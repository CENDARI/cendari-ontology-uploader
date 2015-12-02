package org.cendari.ontology.upload;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.cendari.ontology.utility.NextStep;
import org.cendari.ontology.utility.Utility;

// Extend HttpServlet class
public class DatasetDescriptionUploadIgnoreWarning extends HttpServlet {
	// Method to handle POST method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Utility.getSessionVariable(request, "username") == null || Utility.getSessionVariable(request, "sysadmin") == null || Utility.getSessionVariable(request, "sessionKey") == null) { 
			Utility.setSessionVariable(request, "datasetCreationAlertMessage", "The dataset was not created! Please login first.");
			NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
		}
		else {
			if (Utility.getSessionVariable(request, "alertMessage") == null) {
				if (Utility.createDatasetInCKAN(request, "http://localhost:42042/v1/sets") == true) {
					//response.sendRedirect(request.getContextPath() + "/uploadoriginalfiles.jsp");
					NextStep.goToNextPage(request, response, request.getContextPath()+"/uploadoriginalfiles.jsp");
				}
				else {
					Utility.setSessionVariable(request, "datasetCreationAlertMessage", "Dataset was not created!");
					//request.getRequestDispatcher(request.getContextPath() + "/uploaddatasetdescription.jsp").forward(request, response);
					NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
				}
			}
			else {
				//NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
				request.getRequestDispatcher("uploaddatasetdescription.jsp").forward(request, response);
			}
		}
	}
}
