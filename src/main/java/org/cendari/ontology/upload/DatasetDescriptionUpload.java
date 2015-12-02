package org.cendari.ontology.upload;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.cendari.ontology.utility.NextStep;
import org.cendari.ontology.utility.Utility;

// Extend HttpServlet class
public class DatasetDescriptionUpload extends HttpServlet {
	// Method to handle POST method request.
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Utility.getSessionVariable(request, "username") == null || Utility.getSessionVariable(request, "sysadmin") == null || Utility.getSessionVariable(request, "sessionKey") == null) { 
			Utility.setSessionVariable(request, "datasetCreationAlertMessage", "The dataset was not created! Please login first.");
			NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
		}
		else {
		
			if (request.getParameter("datasetTitle") != null && request.getParameter("datasetTitle") != "") {
				Utility.setSessionVariable(request, "datasetTitle", request.getParameter("datasetTitle"));
			}
			
			if (request.getParameter("datasetDescription") != null) {
				Utility.setSessionVariable(request, "datasetDescription", request.getParameter("datasetDescription"));
			}
			
			if (request.getParameter("dataspaceId") != null && request.getParameter("dataspaceId") != "") {
				Utility.setSessionVariable(request, "dataspaceId", request.getParameter("dataspaceId"));
			}
							
			Utility.setSessionVariable(request, "isDatasetCreated", "false");
		
			//FileUploadHandler handler = new FileUploadHandler();
			//handler.createDatasetInCKAN(request, ontologytitle, description, license, rightsdescription, sourceuri);
		
			boolean isDatasetTitleExisted = Utility.checkDatasetTitleExistence(request, Utility.getSessionVariable(request, "datasetTitle").toString(), "http://localhost:42042/v1/dataspaces/"+Utility.getSessionVariable(request, "dataspaceId")+"/sets");
			
			//If everything is OK, start to crate dataset.
			if (Utility.getSessionVariable(request, "alertMessage") == null) {
				//If dataset title is already existed in CKAN, set warning message to warn the user before proceeding. If dataset title is not existed, create a new dataset in CKAN.
				if (isDatasetTitleExisted == true) {
					Utility.setSessionVariable(request, "datasetTitleWarning", "The dataset title \""+Utility.getSessionVariable(request, "datasetTitle")+"\" exists in the CKAN server. Please press \"OK\" if you want to create a new data set with that title. Otherwise, please press \"Cancel\".");
					//request.getRequestDispatcher("uploaddatasetdescription.jsp").forward(request, response);
					NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
				} else {
					//If a new dataset has been created successfully, go to next page.
					if (Utility.createDatasetInCKAN(request, "http://localhost:42042/v1/sets") == true) {
						//response.sendRedirect(request.getContextPath() + "/uploadoriginalfiles.jsp");
						NextStep.goToNextPage(request, response, request.getContextPath()+"/uploadoriginalfiles.jsp");
					}
					//If a new dataset has NOT been created successfully, send alert message to the user and return to uploaddatasetdescription.jsp.
					else {
						System.out.println("dataset was not created.");
						Utility.setSessionVariable(request, "datasetCreationAlertMessage", "Dataset was not created!");
						//request.getRequestDispatcher(request.getContextPath() + "/uploaddatasetdescription.jsp").forward(request, response);
						NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
					}
				}
			}
			//If there is any alert, show it to the user and return to uploaddatasetdescription.jsp.
			else {
				request.getRequestDispatcher("uploaddatasetdescription.jsp").forward(request, response);
			}
		}
	}
}