package org.cendari.ontology.utility;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class NextStep extends HttpServlet {
 
	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("nextPage").equals("datasetdescription")) {
			//HttpSession session = request.getSession(true);
			//session.invalidate();
			response.sendRedirect(request.getContextPath() + "/uploaddatasetdescription.jsp");
		}
		else if (request.getParameter("nextPage").equals("uploadoriginalfiles")) {
			response.sendRedirect(request.getContextPath() + "/uploadoriginalfiles.jsp");
		}
		else if (request.getParameter("nextPage").equals("uploadtransformedfiles")) {
			response.sendRedirect(request.getContextPath() + "/uploadtransformedfiles.jsp");
		}
		else if (request.getParameter("nextPage").equals("uploadadditionalfiles")) {
			response.sendRedirect(request.getContextPath() + "/uploadadditionalfiles.jsp");
		}
		else if (request.getParameter("nextPage").equals("finished")) {
			response.sendRedirect(request.getContextPath() + "/uploadfinished.jsp");
		}
		else if (request.getParameter("nextPage").equals("createanotherdataset")) {
			HttpSession session = request.getSession(true);
			session.removeAttribute("datasetId");
			//session.invalidate();
			response.sendRedirect(request.getContextPath() + "/uploaddatasetdescription.jsp");
		}
		else if (request.getParameter("nextPage").equals("listdatasets")) {
			//HttpSession session = request.getSession(true);
			//session.invalidate();
			response.sendRedirect(request.getContextPath() + "/listdatasets.jsp");
		}
		else if (request.getParameter("nextPage").equals("listdataspaces")) {
			//HttpSession session = request.getSession(true);
			//session.invalidate();
			response.sendRedirect(request.getContextPath() + "/listdataspaces.jsp");
		}
	}
}