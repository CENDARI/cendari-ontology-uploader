package org.cendari.ontology.upload;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileUploadException;

// Extend HttpServlet class
public class TransformedFilesUpload extends HttpServlet {
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		FileUploadHandler.INSTANCE.processUploadRequest(request, response, "Transformed File");

	}
}