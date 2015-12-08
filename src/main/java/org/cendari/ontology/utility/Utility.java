package org.cendari.ontology.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Utility {
	private static final SecureRandom random = new SecureRandom();
	
	public static final String tempUploadDirectory = "/tmp/ontologyuploader";
	
	public static void setSessionVariable(HttpServletRequest request, String name, String value) {
		HttpSession session = request.getSession(true);
		session.setAttribute(name, value);
	}
	
	public static Object getSessionVariable(HttpServletRequest request, String name) {
		HttpSession session = request.getSession(true);
		return session.getAttribute(name);
	}
	
	public static boolean checkDatasetTitleExistence(HttpServletRequest request, String datasetTitle, String page) {
		try {
			String sessionKey = "";
			if (getSessionVariable(request, "sessionKey") != null && getSessionVariable(request, "sessionKey").toString() != "null") {
				sessionKey = getSessionVariable(request, "sessionKey").toString();
			}
			else {
				setSessionVariable(request, "alertMessage", "Please log in first.");
			}
			
			URL url = new URL (page);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        connection.setRequestProperty ("Authorization", sessionKey);
	        int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			if (responseCode < 400) {
				InputStream content = (InputStream)connection.getInputStream();
		        BufferedReader in   = 
		            new BufferedReader (new InputStreamReader(content));
				
		        String line = null;
		        String nextPage = "";
		        while ((line = in.readLine()) != null) {
		            //System.out.println(line);
		            if (line.contains("\"title\":")) {
		            	//dataset.setTitle(line.substring(line.indexOf(":")+3, line.lastIndexOf("\"")));
		            	if (line.substring(line.indexOf(":")+3, line.lastIndexOf("\"")).equals(datasetTitle)) {
		            		return true;
		            	}	
		            }
		            else if (line.contains("\"nextPage\":")) {
		            	nextPage = line.substring(line.indexOf(":")+3, line.lastIndexOf("\""));
		            }
		        }
		        if (nextPage != "") {
		        	return checkDatasetTitleExistence(request, datasetTitle, nextPage);
		        }
				return false;
			}
			else {
				request.getSession().setAttribute("alertMessage", responseCode+" error!");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
    }
	
	public static boolean checkDatasetNameExistence(HttpServletRequest request, String datasetName, String page) throws IOException {
		HttpSession session = request.getSession(true);
		String authorization = session.getAttribute("authorization").toString();
		
		URL url = new URL (page);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty ("Authorization", authorization);
        InputStream content = (InputStream)connection.getInputStream();
        BufferedReader in   = 
            new BufferedReader (new InputStreamReader (content));
		
        String line = null;
        String nextPage = "";
        while ((line = in.readLine()) != null) {
            //System.out.println(line);
            if (line.contains("\"name\":")) {
            	//dataset.setTitle(line.substring(line.indexOf(":")+3, line.lastIndexOf("\"")));
            	if (line.substring(line.indexOf(":")+3, line.lastIndexOf("\"")).equals(datasetName)) {
            		return true;
            	}	
            }
            else if (line.contains("\"nextPage\":")) {
            	nextPage = line.substring(line.indexOf(":")+3, line.lastIndexOf("\""));
            	//System.out.println("[DatasetsList] nextPage: "+nextPage);
            }
        }
        if (nextPage != "") {
        	return checkDatasetNameExistence(request, datasetName, nextPage);
        }
		return false;
	}
	
	public static String createDatasetName() {
		return new BigInteger(130, random).toString(32);
	}
	
	public static boolean createDatasetInCKAN(HttpServletRequest request, String setsUrl) {
		String datasetTitle = "";
		if (getSessionVariable(request, "datasetTitle") != null) {
			datasetTitle = getSessionVariable(request, "datasetTitle").toString();
		}
		
		String datasetDescription = "";
		if (getSessionVariable(request, "datasetDescription") != null) {
			datasetDescription = getSessionVariable(request, "datasetDescription").toString();
		}
		
		String dataspaceId = "";
		if (getSessionVariable(request, "dataspaceId") != null) {
			dataspaceId = getSessionVariable(request, "dataspaceId").toString();
		}
		else {
			return false;
		}
		
		String sessionKey = "";
		if (getSessionVariable(request, "sessionKey") != null) {
			sessionKey = getSessionVariable(request, "sessionKey").toString();
		}
		else {
			return false;
		}
		
		try {
			boolean isDatasetCreated = false;
			//int number = 1;
            //do {
				URL obj = new URL(setsUrl);
        		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        		//add reuqest header
        		con.setRequestMethod("POST");
        		con.setRequestProperty("User-Agent", "Mozilla/5.0");
        		con.setRequestProperty("Content-Type","application/json");
        		con.setRequestProperty ("Authorization", sessionKey);
        		
        		String datasetName = createDatasetName();
        		/*while (checkDatasetNameExistence(request, datasetName, setsUrl) == true) {
        			datasetName = createDatasetName();
        		}*/
        		
        		JSONObject urlParameters = new JSONObject();
        		urlParameters.put("name", datasetName);
        		urlParameters.put("title", datasetTitle);
        		urlParameters.put("description", datasetDescription);
        		urlParameters.put("dataspaceId", dataspaceId);
        		
        		// Send post request
        		con.setDoOutput(true);
        		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        		wr.writeBytes(urlParameters.toString());
        		wr.flush();
        		wr.close();
        		
        		int responseCode = con.getResponseCode();
        		//System.out.println("Response Code : " + responseCode);
        		
        		if (responseCode == 201) {
        			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		            String line = null;
		            while ((line = in.readLine()) != null) {
		            	//System.out.println(line);
		            	//appendLogFile(request.getServletContext().getRealPath("/")+"/upload/log.txt", line);
		            	if (line.contains("active")) {
		            		isDatasetCreated = true;
		            	}
		            	else if (line.contains("url")){
		            		String url = line.substring(line.indexOf("http"), line.lastIndexOf("\""));
		            		//System.out.println("Dataset URL: "+url);
		            		setSessionVariable(request, "datasetURL", url);
		            		
		            		String datasetId = line.substring(line.lastIndexOf("/")+1, line.lastIndexOf("\""));
		            		//System.out.println("Dataset name: "+datasetId);
		            		setSessionVariable(request, "datasetId", datasetId);
		            	}
		            }
		            setSessionVariable(request, "isDatasetCreated", "true");
		            return true;
        		}
        		else {
        			setSessionVariable(request, "isDatasetCreated", "false");
        			setSessionVariable(request, "alertMessage", responseCode+" error!");
        			//System.out.println("alertMessage: "+responseCode+" error!");
        			return false;
        		}
            //}
            //while (isDatasetCreated == false);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		return false;
	}
	
	public static void getFilesMetadata(String authorization, String page, ArrayList<JSONObject> jsonObjectList) {
		try {
			URL url = new URL (page);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        //connection.setRequestProperty("Authorization", "c821daf7-9439-4dcd-a203-20a57eafdb66");
	        connection.setRequestProperty("Authorization", authorization);
	        InputStream content = (InputStream)connection.getInputStream();
	        BufferedReader in   = 
	            new BufferedReader (new InputStreamReader (content));
			
	        String line = null;
	        String nextPage = "";
	        String responseBody = "";
	        boolean isThisEndObject = false; 
	        while ((line = in.readLine()) != null) {
	        	//System.out.println(line);
	        	
	        	if (line.contains("\"nextPage\":")) {
	            	nextPage = line.substring(line.indexOf(":")+3, line.lastIndexOf("\""));
	            	//System.out.println("next page: "+nextPage);
	            }
	        	
	        	if (line.contains("\"end\": true")) {
	        		isThisEndObject = true;
	        		//System.out.println("not adding: "+line);
	        	} 
	        	else {
	        		responseBody = responseBody + line;
	        	}
	        	
	        }
	        if (isThisEndObject == false) {
	        	Object obj = JSONValue.parse(responseBody);
	        	JSONObject jsonObject = (JSONObject)obj;
	        	jsonObjectList.add(jsonObject);
	        }
	        if (nextPage != "") {
	        	getFilesMetadata(authorization, nextPage, jsonObjectList);
	        }
	        //return response;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void deleteFile(String sessionKey, String fileDeleteUrl) {
		
		try {
	        URL url = new URL (fileDeleteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", sessionKey);
	        
            int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			/*String line = null;
	        while ((line = in.readLine()) != null) {
	            System.out.println(line);
	        }*/
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private static void setHttpsHeader(HttpServletRequest request) {
		if (request.getHeader("referer") != null && request.getHeader("referer").contains("https:")) {
			setSessionVariable(request, "isHttpsEnabled", "true");
		}
		else {
			setSessionVariable(request, "isHttpsEnabled", "false");
		}
	}
	
	public static void getUserSessionInfo(HttpServletRequest request, String eppn, String mail, String cn) {
		try {
			
			setSessionVariable(request, "host", request.getHeader("host"));
			//setSessionVariable(request, "sessionKey", "c821daf7-9439-4dcd-a203-20a57eafdb66");
			
			setHttpsHeader(request);
			
			HttpSession session = request.getSession(true);
			URL url = new URL ("http://localhost:42042/v1/session");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			StringBuffer requestBody = new StringBuffer();
			
			requestBody.append("{\"eppn\":\""+eppn+"\", \"mail\":\""+mail+"\", \"cn\":\""+cn+"\"}");
			
			wr.writeBytes(requestBody.toString());
			wr.flush();
			wr.close();
			int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line = "";
	        String response = "";
	        while ((line = in.readLine()) != null) {
	            //System.out.println(line);
	            response = response + line;
	        }
	        
	        Object obj = JSONValue.parse(response);
        	JSONObject jsonObject = (JSONObject)obj;
        	if (jsonObject.get("username") != null) {
        		setSessionVariable(request, "username", jsonObject.get("username").toString());
        	}
        	if (jsonObject.get("sessionKey") != null) {
        		setSessionVariable(request, "sessionKey", jsonObject.get("sessionKey").toString());
        	}
        	if (jsonObject.get("sysadmin") != null) {
        		setSessionVariable(request, "sysadmin", jsonObject.get("sysadmin").toString());
        	}
        	
        } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static JSONArray getDataspacesMetadata(HttpServletRequest request, String sessionKey, String dataspaceUrl) {
		JSONArray dataspaceArray = null;
		try {
			URL url = new URL (dataspaceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty ("Authorization", sessionKey);
            
            int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			String responseBody = "";
			if (responseCode == 200) {
	            InputStream content = (InputStream)connection.getInputStream();
	            BufferedReader in = new BufferedReader(new InputStreamReader(content));
				
	            String line = null;
	            String nextPage = "";
	            while ((line = in.readLine()) != null) {
	                //System.out.println(line);
	                responseBody = responseBody + line;
	                if (line.contains("\"nextPage\":")) {
	                	nextPage = line.substring(line.indexOf(":")+3, line.lastIndexOf("\""));
	                }
	            }
	            Object obj = JSONValue.parse(responseBody);
	        	JSONObject jsonObject = (JSONObject)obj;
	        	dataspaceArray = (JSONArray)jsonObject.get("data");
	            if (nextPage != "") {
	            	JSONArray nextPageDataspaceArray = getDataspacesMetadata(request, sessionKey, nextPage);
	            	if (nextPageDataspaceArray != null) {
	            		dataspaceArray.addAll(nextPageDataspaceArray);
	            	}
	            }
	        }
			else {
				setSessionVariable(request, "alertMessage", responseCode + " error!");
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataspaceArray;
	}
	
	public static JSONArray getDatasetsMetadata(HttpServletRequest request, String sessionKey, String datasetUrl) {
		JSONArray datasetArray = null;
		try {
			URL url = new URL (datasetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty ("Authorization", sessionKey);
            
            int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			String responseBody = "";
			if (responseCode == 200) {
	            InputStream content = (InputStream)connection.getInputStream();
	            BufferedReader in = new BufferedReader(new InputStreamReader(content));
				
	            String line = null;
	            String nextPage = "";
	            while ((line = in.readLine()) != null) {
	                //System.out.println(line);
	                responseBody = responseBody + line;
	                if (line.contains("\"nextPage\":")) {
	                	nextPage = line.substring(line.indexOf(":")+3, line.lastIndexOf("\""));
	                	//System.out.println("nextPage: "+nextPage);
	                }
	                /*if (!line.contains("\"end\": true")) {
	                	responseBody = responseBody + line;
		            }*/
	            }
	            Object obj = JSONValue.parse(responseBody);
	        	JSONObject jsonObject = (JSONObject)obj;
	        	datasetArray = (JSONArray)jsonObject.get("data");
	            if (nextPage != "") {
	            	JSONArray nextPageDatasetArray = getDatasetsMetadata(request, sessionKey, nextPage);
	            	if (nextPageDatasetArray != null) {
	            		datasetArray.addAll(nextPageDatasetArray);
	            	}
	            }
	        }
			else {
				setSessionVariable(request, "alertMessage", responseCode + " error!");
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return dataspaceNames;
		return datasetArray;
	}
	
	public static JSONObject getADatasetMetadata(HttpServletRequest request, String sessionKey, String datasetUrl) {
		JSONObject jsonObject = null;
		try {
			URL url = new URL (datasetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty ("Authorization", sessionKey);
            
            int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			String responseBody = "";
			if (responseCode == 200) {
	            InputStream content = (InputStream)connection.getInputStream();
	            BufferedReader in = new BufferedReader(new InputStreamReader(content));
				
	            String line = null;
	            while ((line = in.readLine()) != null) {
	                System.out.println(line);
	                responseBody = responseBody + line;
	            }
	            Object obj = JSONValue.parse(responseBody);
	        	jsonObject = (JSONObject)obj;
	        }
			else {
				setSessionVariable(request, "alertMessage", responseCode + " error!");
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return dataspaceNames;
		return jsonObject;
	}
	
	public static JSONArray getResourcesMetadata(HttpServletRequest request, String sessionKey, String resourceUrl) {
		JSONArray resourceArray = null;
		try {
			URL url = new URL (resourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty ("Authorization", sessionKey);
            
            int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			String responseBody = "";
			if (responseCode == 200) {
	            InputStream content = (InputStream)connection.getInputStream();
	            BufferedReader in = new BufferedReader(new InputStreamReader(content));
				
	            String line = null;
	            String nextPage = "";
	            String resourceState = "";
	            while ((line = in.readLine()) != null) {
	            	if (line.contains("\"nextPage\":")) {
	                	nextPage = line.substring(line.indexOf(":")+3, line.lastIndexOf("\""));
	                }
	            	else if (line.contains("\"name\":")) {
	            		//System.out.println("[Utility.getResourcesMetadata()] resource name: "+line);
	            	}
	            	//If a resource shows "deleted" on CKAN's website, the CENDARI REST API still returns active. So the resource's state needs to be checked.
	                else if (line.contains("\"viewDataUrl\":")) {
	                	resourceState = getResourceState(request, line.substring(line.indexOf(":")+3, line.lastIndexOf("\"")));
	                }
	            	//If a resource shows "deleted" on CKAN's website, the CENDARI REST API still returns active. So the state needs to be updated.
	                else if (line.contains("\"state\":")) {
	                	//System.out.println("[Utility.getResourcesMetadata()] state from REST API: "+line);
	                	if (line.contains("active") && resourceState.equals("deleted")) {
	                		line = line.replace("active", "deleted");
	                		//System.out.println("[Utility.getResourcesMetadata()] state from REST API after update: "+line);
	                	}
	                }
	                responseBody = responseBody + line;
	            }
	            Object obj = JSONValue.parse(responseBody);
	        	JSONObject jsonObject = (JSONObject)obj;
	        	resourceArray = (JSONArray)jsonObject.get("data");
	            if (nextPage != "") {
	            	JSONArray nextPageResourceArray = getResourcesMetadata(request, sessionKey, nextPage);
	            	if (nextPageResourceArray != null) {
	            		resourceArray.addAll(nextPageResourceArray);
	            	}
	            }
	        }
			else {
				setSessionVariable(request, "alertMessage", responseCode + " error!");
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return dataspaceNames;
		return resourceArray;
	}
		
	private static String getResourceState(HttpServletRequest request, String resourcePage) {
		try {
	        URL url = new URL (resourcePage);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            
            connection.setRequestProperty("Authorization", getSessionVariable(request, "sessionKey").toString());
	        
            int responseCode = connection.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
			String responseBody = "";
			if (responseCode == 200) {
		        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        String line = null;
		        while ((line = in.readLine()) != null) {
		        	if (line.contains("<th scope=\"row\">state</th>")) {
		        		if (line.contains("active")) {
		            		return "active";
		            	}
		            	else if (line.contains("deleted")) {
		            		return "deleted";
		            	}
		            }
		        }
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return "others";
	}
}
