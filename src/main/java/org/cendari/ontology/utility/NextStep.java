package org.cendari.ontology.utility;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class NextStep extends HttpServlet {
 
	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = "";
		HttpSession session = request.getSession(true);
		if (request.getParameter("nextPage").equals("datasetdescription")) {
			nextPage = request.getContextPath() + "/uploaddatasetdescription.jsp";
		}
		else if (request.getParameter("nextPage").equals("uploadoriginalfiles")) {
			nextPage = request.getContextPath() + "/uploadoriginalfiles.jsp";
		}
		else if (request.getParameter("nextPage").equals("uploadtransformedfiles")) {
			nextPage = request.getContextPath() + "/uploadtransformedfiles.jsp";
		}
		else if (request.getParameter("nextPage").equals("uploadadditionalfiles")) {
			nextPage = request.getContextPath() + "/uploadadditionalfiles.jsp";
		}
		else if (request.getParameter("nextPage").equals("finished")) {
			nextPage = request.getContextPath() + "/uploadfinished.jsp";
		}
		else if (request.getParameter("nextPage").equals("createanotherdataset")) {
			session.removeAttribute("datasetId");
			nextPage = request.getContextPath() + "/uploaddatasetdescription.jsp";
		}
		else if (request.getParameter("nextPage").equals("listdatasets")) {
			nextPage = request.getContextPath() + "/listdatasets.jsp";
		}
		else if (request.getParameter("nextPage").equals("listdataspaces")) {
			//HttpSession session = request.getSession(true);
			//session.invalidate();
			nextPage = request.getContextPath() + "/listdataspaces.jsp";
		}

	    goToNextPage(request, response, nextPage);
	}
	
	private static String getHttpsUrl(String host, String nextPage) {
		String httpsUrl = "https://"+host+nextPage;
		return httpsUrl;
	}
	
	public static void goToNextPage(HttpServletRequest request, HttpServletResponse response, String nextPage) throws IOException, ServletException {
		String host = request.getHeader("host");
		System.out.println("nextPage: "+nextPage);
		if (host != null && !host.contains("localhost")) {
			response.sendRedirect(getHttpsUrl(host, nextPage));
			//request.getRequestDispatcher(getHttpsUrl(host, nextPage)).forward(request, response);
		}
		else {
			response.sendRedirect(nextPage);
			//request.getRequestDispatcher(nextPage).forward(request, response);
		}
	}
}