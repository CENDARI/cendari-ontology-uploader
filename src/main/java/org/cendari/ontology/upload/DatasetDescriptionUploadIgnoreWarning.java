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
		System.out.println("---DatasetDescriptionUploadIrnoreWarning---");
		System.out.println("datasetTitle: "+ Utility.getSessionVariable(request, "datasetTitle"));
		System.out.println("datasetDescription: "+ Utility.getSessionVariable(request, "datasetDescription"));
		System.out.println("dataspaceId: "+ Utility.getSessionVariable(request, "dataspaceId"));
		System.out.println("sessionKey: "+ Utility.getSessionVariable(request, "sessionKey"));
		
		/*if (Utility.createDatasetInCKAN(request) == true) {
			response.sendRedirect(request.getContextPath() + "uploadoriginalfiles.jsp");
		}
		else {
			request.getSession().setAttribute("alertMessage", "The dataset was not created successfully!");
			request.getRequestDispatcher("uploaddatasetdescription.jsp").forward(request, response);
		}*/
		
		if (Utility.getSessionVariable(request, "alertMessage") == null) {
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
		else {
			System.out.println(Utility.getSessionVariable(request, "alertMessage"));
			//request.getRequestDispatcher(request.getContextPath() + "/uploaddatasetdescription.jsp").forward(request, response);
			NextStep.goToNextPage(request, response, request.getContextPath()+"/uploaddatasetdescription.jsp");
		}
	}
}
