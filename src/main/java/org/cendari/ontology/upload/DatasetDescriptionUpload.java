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
		
		System.out.println("---DatasetDescriptionUpload---");
		System.out.println("datasetTitle: "+ Utility.getSessionVariable(request, "datasetTitle"));
		System.out.println("datasetDescription: "+ Utility.getSessionVariable(request, "datasetDescription"));
		System.out.println("dataspaceId: "+ Utility.getSessionVariable(request, "dataspaceId"));
		System.out.println("sessionKey: "+ Utility.getSessionVariable(request, "sessionKey"));
		//FileUploadHandler handler = new FileUploadHandler();
		//handler.createDatasetInCKAN(request, ontologytitle, description, license, rightsdescription, sourceuri);
		
		boolean isDatasetTitleExisted = Utility.checkDatasetTitleExistence(request, Utility.getSessionVariable(request, "datasetTitle").toString(), "http://localhost:42042/v1/dataspaces/"+Utility.getSessionVariable(request, "dataspaceId")+"/sets");
		
		if (Utility.getSessionVariable(request, "alertMessage") == null) {
			if (isDatasetTitleExisted == true) {
				Utility.setSessionVariable(request, "datasetTitleWarning", "The dataset title \""+Utility.getSessionVariable(request, "datasetTitle")+"\" exists in the CKAN server. Please press \"OK\" if you want to create a new data set with that title. Otherwise, please press \"Cancel\".");
				//request.getRequestDispatcher("uploaddatasetdescription.jsp").forward(request, response);
				NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
			} else {
				if (Utility.createDatasetInCKAN(request, "http://localhost:42042/v1/sets") == true) {
					//response.sendRedirect(request.getContextPath() + "/uploadoriginalfiles.jsp");
					NextStep.goToNextPage(request, response, request.getContextPath()+"/uploadoriginalfiles.jsp");
				}
				else {
					Utility.setSessionVariable(request, "alertMessage", "The dataset was not created successfully!");
					//request.getRequestDispatcher(request.getContextPath() + "/uploaddatasetdescription.jsp").forward(request, response);
					NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
				}
			}
		}
		else {
			System.out.println(Utility.getSessionVariable(request, "alertMessage"));
			request.getRequestDispatcher("uploaddatasetdescription.jsp").forward(request, response);
		}
	}
}