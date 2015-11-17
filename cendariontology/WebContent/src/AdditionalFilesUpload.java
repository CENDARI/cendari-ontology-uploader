
// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class AdditionalFilesUpload extends HttpServlet {
 
  // Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String nextPage = "finished.jsp";
		// Set response content type
		/*response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		String title = "Using GET Method to Read Form Data";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out.println(docType + "<html>\n" +
			"<head><title>" + title + "</title></head>\n" +
			"<body bgcolor=\"#f0f0f0\">\n" +
			"<h1 align=\"center\">" + title + "</h1>\n" +
			"<ul>\n" +
			"  <li><b>Ontology Title</b>: "
			+ request.getParameter("ontologytitle") + "\n" +
			"  <li><b>Descrpition</b>: "
			+ request.getParameter("description") + "\n" +
			"  <li><b>Rights Description</b>: "
			+ request.getParameter("rightsdescription") + "\n" +
			"  <li><b>Source URL</b>: "
			+ request.getParameter("sourceurl") + "\n" +
			"</ul>\n" +
			"</body></html>");*/
		response.sendRedirect(nextPage);
	}
  
	// Method to handle POST method request.
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}