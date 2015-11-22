package org.cendari.ontology.upload;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;


public class OriginalFilesUpload extends HttpServlet {
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     * 
     */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*HttpSession session = request.getSession(true);
		if ((request.getParameter("sessionKey") != null) && request.getParameter("sessionKey") != "") {
			session.setAttribute("sessionKey", request.getParameter("sessionKey"));
		}
		else {
			session.setAttribute("authorization", "c821daf7-9439-4dcd-a203-20a57eafdb66");
		}*/
		
		FileUploadHandler.INSTANCE.processUploadRequest(request, response, "Original File");
		
		
	}
}