package org.cendari.ontology.download;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cendari.ontology.utility.Utility;

public class FileDownload extends HttpServlet {
	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//RequestDispatcher rd = request.getRequestDispatcher("listresources.jsp");
		//rd.forward(request, response);
		Utility.setSessionVariable(request, "resourcePage", request.getParameter("resourcePage"));
		Utility.setSessionVariable(request, "fileDownloadUrl", request.getParameter("fileDownloadUrl"));
		Utility.setSessionVariable(request, "fileName", request.getParameter("fileName"));
		
		//version 1: download file to the web server and then download the file to local computer.
		//getFileFromCkan(request, response, "c821daf7-9439-4dcd-a203-20a57eafdb66",session.getAttribute("fileurl").toString(), session.getAttribute("filename").toString());
		
		//version 2: find out the correct url for the file and redirect the browser to the url.
		//String nextPage = getFileDownloadUrl(session.getAttribute("resourcePage").toString());
		String filePage = getFileDownloadUrl(request, Utility.getSessionVariable(request, "fileDownloadUrl").toString());
		response.sendRedirect(filePage);
	}
	
	private String getFileDownloadUrl(HttpServletRequest request, String resourceUrl) {
		String nextPage = "";
		try {
            //Process p = Runtime.getRuntime().exec("curl -H 'Authorization: c821daf7-9439-4dcd-a203-20a57eafdb66' "+resourceUrl);
            
            URL url = new URL (resourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            
            connection.setRequestProperty("Authorization", Utility.getSessionVariable(request, "sessionKey").toString());
            InputStream content = (InputStream)connection.getInputStream();
                        
            BufferedReader in = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
            /*String line = null;
            while ((line = in.readLine()) != null) {
                if (line.contains("URL: ")) {
                	nextPage = line.substring(line.lastIndexOf("https://"), line.lastIndexOf("</a>"));
                }
            }*/
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		return nextPage;
	}
}
