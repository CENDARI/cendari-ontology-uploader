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
import java.util.HashMap;
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
		ArrayList<JSONObject> resourceObjectsList = new ArrayList<JSONObject>();
		String resourcesUrl = "http://localhost:42042/v1/sets/"+Utility.getSessionVariable(request, "datasetId")+"/resources";
		if (Utility.getSessionVariable(request, "username") == null || Utility.getSessionVariable(request, "sysadmin") == null || Utility.getSessionVariable(request, "sessionKey") == null) { 
			Utility.setSessionVariable(request, "relationshipFileCreationAlertMessage", "The relationship file was not created! Please login first.");
		}
		else {
			Utility.getFilesMetadata(Utility.getSessionVariable(request, "sessionKey").toString(), resourcesUrl, resourceObjectsList);
		}
		
		String datasetUrl = "http://localhost:42042/v1/sets/" + Utility.getSessionVariable(request, "datasetId");
		JSONObject datasetObject = Utility.getADatasetMetadata(request, Utility.getSessionVariable(request, "sessionKey").toString(), datasetUrl); 
		synchronized (this) {
			deleteRelationshipFiles(request, resourceObjectsList);
			File relationshipFile = generateRelationshipFile(request, datasetObject, resourceObjectsList);
			FileUploadHandler.INSTANCE.uploadFileToCKAN(request, relationshipFile, "Relationship File");
			if (relationshipFile.exists()) {
	    		relationshipFile.delete();
	    	}
		}
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
	
	private File generateRelationshipFile(HttpServletRequest request, JSONObject datasetObject, ArrayList<JSONObject> jsonObjectList) throws IOException {
		HashMap<String, String> originalFileMap = new HashMap<String, String>();
		String filePath = request.getServletContext().getRealPath("/")+"/upload/relationship.xml";
		filePath = Utility.tempUploadDirectory + "/relationship.xml";
		File relationshipFile = new File(filePath);
		
		if (!relationshipFile.getParentFile().exists()) {
			relationshipFile.getParentFile().mkdirs();
		}
		
		if (!relationshipFile.exists()) {
			relationshipFile.createNewFile();
		}

		FileWriter fw = new FileWriter(relationshipFile.getAbsolutePath());
		//FileWriter fw = new FileWriter(relationshipFile);
		
		//System.out.println("relationshipFile.getAbsolutePath(): "+relationshipFile.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bw.newLine();
		
		bw.write("<" + datasetObject.get("resources").toString() + "/originalRM> <ore:describes> <" + datasetObject.get("resources").toString() + "/originalAgg>");
		bw.newLine();
		bw.write("<" + datasetObject.get("resources").toString() + "/transformedRM> <ore:describes> <" + datasetObject.get("resources").toString() + "/transformedAgg>");
		bw.newLine();
		bw.write("<" + datasetObject.get("resources").toString() + "/additionalRM> <ore:describes> <" + datasetObject.get("resources").toString() + "/additionalAgg>");
		bw.newLine();
		
		//bw.write("<" + datasetObject.get("resources").toString() + "/originalRM> <dcterms:creator> <" + Utility.getSessionVariable(request, "username") + ">");
		//bw.newLine();
		
		
		JSONObject originalFile = null;
		JSONObject transformedFile = null;
		JSONObject additionalFile = null;
		
		ListIterator<JSONObject> it = jsonObjectList.listIterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject)it.next();
	        JSONArray data = (JSONArray) jsonObject.get("data");
	        for (int i = 0; i < data.size(); i++) {
	        	//System.out.println(data.get(i));
	        	
	        	//bw.write(data.get(i).toString().replace("\\/", "/"));
	        	//bw.newLine();
	        	//bw.newLine();
	        	
	        	JSONObject fileInfo = (JSONObject) data.get(i);
	        	String fileDescription = fileInfo.get("description").toString();
	        	if (fileDescription.contains("Original")) {
	        		originalFile = fileInfo;
	        		bw.write("<" + datasetObject.get("resources").toString() + "/originalAgg> <ore:aggregates> <" + originalFile.get("url").toString() + "/" + originalFile.get("name") +">");
	        		bw.newLine();
	        		originalFile.put(originalFile.get("name").toString(), originalFile.get("url").toString()+"/"+originalFile.get("name"));
	        	}
	        	else if (fileDescription.contains("Transformed")) {
	        		transformedFile = fileInfo;
	        		bw.write("<" + datasetObject.get("resources").toString() + "/transformedAgg> <ore:aggregates> <" + transformedFile.get("url").toString() + "/" + transformedFile.get("name") +">");
	        		bw.newLine();
	        		if (originalFile.get(transformedFile.get("name")) != null) {
	        			bw.write("<" + transformedFile.get("url").toString() + "/" + transformedFile.get("name") +"> <dcterms:source> <" + originalFile.get(transformedFile.get("name")) +">");
		        		bw.newLine();
	        		}
	        	}
	        	else if (fileDescription.contains("Additional")) {
	        		additionalFile = fileInfo;
	        		bw.write("<" + datasetObject.get("resources").toString() + "/additionalAgg> <ore:aggregates> <" + additionalFile.get("url").toString() + "/" + additionalFile.get("name") +">");
	        		bw.newLine();	
	        	}
	        }
		}
		bw.flush();
		bw.close();
		return relationshipFile;
	}
}
