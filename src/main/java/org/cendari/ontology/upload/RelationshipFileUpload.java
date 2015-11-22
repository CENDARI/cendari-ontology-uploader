package org.cendari.ontology.upload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cendari.ontology.utility.NextStep;
import org.cendari.ontology.utility.Utility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RelationshipFileUpload extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		String page = "http://localhost:42042/v1/sets/"+Utility.getSessionVariable(request, "datasetId")+"/resources";
		Utility.getFilesMetadata(Utility.getSessionVariable(request, "sessionKey").toString(), page, jsonObjectList);
		
		deleteRelationshipFiles(request, jsonObjectList);
		File relationshipFile = generateRelationshipFile(request, jsonObjectList);
		FileUploadHandler.INSTANCE.uploadFileToCKAN(request, relationshipFile, "Relationship File");
		//response.sendRedirect(request.getContextPath() + "/uploadfinished.jsp");
		NextStep.goToNextPage(request, response, request.getContextPath()+"/uploadfinished.jsp");
	}
	
	private String deleteRelationshipFiles(HttpServletRequest request, ArrayList<JSONObject> jsonObjectList) {
		String relationshipFileUrl = "";
		ListIterator<JSONObject> it = jsonObjectList.listIterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject)it.next();
	        JSONArray data = (JSONArray) jsonObject.get("data");
	        for (int i = 0; i < data.size(); i++) {
	        	JSONObject fileInfo = (JSONObject) data.get(i);
	        	if (fileInfo.get("name").toString().equals("relationship.xml")) {
	        		relationshipFileUrl = fileInfo.get("url").toString();
	        		Utility.deleteFile(Utility.getSessionVariable(request, "sessionKey").toString(), relationshipFileUrl);
	        	}
	        }
		}
		return relationshipFileUrl;
	}
	
	private File generateRelationshipFile(HttpServletRequest request, ArrayList<JSONObject> jsonObjectList) throws IOException {
		File relationshipFile = new File("/test/relationship.xml");
		FileWriter fw = new FileWriter(relationshipFile.getAbsolutePath());
		//System.out.println("relationshipFile.getAbsolutePath(): "+relationshipFile.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter(fw);
		
		JSONObject originalFile = null;
		JSONObject transformedFile = null;
		JSONObject additionalFile = null;
		
		ListIterator<JSONObject> it = jsonObjectList.listIterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject)it.next();
	        System.out.println(jsonObject);
	        JSONArray data = (JSONArray) jsonObject.get("data");
	        for (int i = 0; i < data.size(); i++) {
	        	System.out.println(data.get(i));
	        	
	        	//bw.write(data.get(i).toString().replace("\\/", "/"));
	        	//bw.newLine();
	        	//bw.newLine();
	        	
	        	JSONObject fileInfo = (JSONObject) data.get(i);
	        	String fileDescription = fileInfo.get("description").toString();
	        	if (fileDescription.contains("Original")) {
	        		originalFile = fileInfo;
	        	}
	        	else if (fileDescription.contains("Transformed")) {
	        		transformedFile = fileInfo;
	        	}
	        	else if (fileDescription.contains("Additional")) {
	        		additionalFile = fileInfo;
	        	}
	        }
	        if (originalFile != null) {
        		if (transformedFile != null) {
            		bw.write("<"+transformedFile.get("url")+"> <dcterms:source> <"+originalFile.get("url")+">");
            		bw.newLine();
    	        	bw.newLine();
            	}
            	if (additionalFile != null)	{
            		bw.write("<"+transformedFile.get("url")+"> <dcterms:source> <"+originalFile.get("url")+">");
            		bw.newLine();
    	        	bw.newLine();
            	}
        	}
        	
		}
		bw.flush();
		bw.close();
		return relationshipFile;
	}
}
