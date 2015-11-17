package org.cendari.ontology.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cendari.ontology.utility.Utility;

public class ResourcesList extends HttpServlet {
	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("datasetId") != null) {
			Utility.setSessionVariable(request, "datasetId", request.getParameter("datasetId"));
		}
		if (request.getParameter("datasetTitle") != null) {
			Utility.setSessionVariable(request, "datasetTitle", request.getParameter("datasetTitle"));
		}
		if (request.getParameter("resources") != null) {
			Utility.setSessionVariable(request, "resources", request.getParameter("resources"));
		}
		response.sendRedirect(request.getContextPath() + "/listresources.jsp");
	}
}
