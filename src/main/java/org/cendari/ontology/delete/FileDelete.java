package org.cendari.ontology.delete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cendari.ontology.download.ResourcesList;
import org.cendari.ontology.utility.Utility;

public class FileDelete extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//RequestDispatcher rd = request.getRequestDispatcher("listresources.jsp");
		//rd.forward(request, response);
		if (Utility.getSessionVariable(request, "sessionKey") != null) {
			Utility.deleteFile(Utility.getSessionVariable(request, "sessionKey").toString(), request.getParameter("fileDeleteUrl"));
		}
		else {
			Utility.setSessionVariable(request, "alertMessage", "Please login first.");
		}
		ResourcesList resourceList = new ResourcesList();
		resourceList.doGet(request, response);
		//response.sendRedirect(session.getAttribute("resources").toString());
	}
}
