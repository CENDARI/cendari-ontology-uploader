package org.cendari.ontology.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cendari.ontology.utility.Utility;

public class DatasetsList extends HttpServlet {
	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("dataspaceId") != null) {
			Utility.setSessionVariable(request, "dataspaceId", request.getParameter("dataspaceId"));
		}
		if (request.getParameter("dataspaceUrl") != null) {
			Utility.setSessionVariable(request, "dataspaceUrl", request.getParameter("dataspaceUrl"));
		}
		if (request.getParameter("dataspaceTitle") != null) {
			Utility.setSessionVariable(request, "dataspaceTitle", request.getParameter("dataspaceTitle"));
		}
		
		response.sendRedirect(request.getContextPath() + "/listdatasets.jsp");
	}	
}
