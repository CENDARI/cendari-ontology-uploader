package org.cendari.ontology.upload;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.cendari.ontology.utility.Utility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public enum FileUploadHandler {
	INSTANCE;
	
	private static final String SAVE_DIR = "upload";
	
	public static FileUploadHandler getInstance() {
		return INSTANCE;
	}
	private void setSessionVariable(HttpServletRequest request, String name, String value) {
		HttpSession session = request.getSession(true);
		session.setAttribute(name, value);
	}
	
	private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
	
	void processUploadRequest(HttpServletRequest request, HttpServletResponse response, String fileType) throws IOException, ServletException {
		// gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;
         
        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.getParentFile().mkdirs();
        }
        
        response.setContentType("application/json");
	    JSONObject finalObj = new JSONObject();
	    JSONArray list = new JSONArray();
	    JSONObject obj = new JSONObject();
        
        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            part.write(savePath + File.separator + fileName);
            File fileToUpload = new File(savePath + File.separator + fileName);
            String status = uploadFileToCKAN(request, fileToUpload, fileType);
            
            if (status.contains("201")) {
       	 		obj.put("name", fileToUpload.getName());
                obj.put("size", fileToUpload.length());
                obj.put("url", "");
                //obj.put("thumbnailUrl", filepath + item.getName());
                obj.put("deleteUrl", "");
                obj.put("deleteType", "DELETE");
       	 	}
       	 	else {
       	 		obj.put("error", status);
       	 	}
            list.add(obj);
        }
        finalObj.put("files", list);
    }
	
	/*void processUploadRequest(HttpServletRequest request, HttpServletResponse response, String fileType) throws IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
	        throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
	    }
		
		
		String fileDescription = fileType;
		
	    ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
	    PrintWriter writer = response.getWriter();
	    response.setContentType("application/json");
	    JSONObject finalObj = new JSONObject();
	    JSONArray list = new JSONArray();
	    try {
	    	//List<FileItem> items = null;
	        List<FileItem> items = uploadHandler.parseRequest(request);
	        //uploadFilesToCKAN(request, items);
	        for (FileItem item : items) {
	        	if (!item.isFormField()) {
	            	//String filePath = request.getServletContext().getRealPath("/")+"/upload/";
	            	String filePath = request.getServletContext().getRealPath("/");
	            	//System.out.println("filePath: "+filePath);
	            	//filePath = request.getSession().getServletContext().getRealPath("/");
	            	//System.out.println("filePath: "+filePath);
	            	filePath = "/test/";
	            	
	        		File directory = new File(filePath);
	            	boolean isDirectoryExisted = false;
	            	if (!directory.exists()) {
	            		if (directory.mkdir()) {
	            			System.out.println("Directory is created!");
	            			isDirectoryExisted = true;
		            	} else {
	            			System.out.println("Failed to create directory!");
	            		}
	            	}
	            	else {
	            		isDirectoryExisted = true;
	            	}
	            	File file = null;
	            	if (isDirectoryExisted) {
	            		file = new File(filePath, item.getName());
	            		
	            		if (!file.exists()) {
	        				file.createNewFile();
	        			}
	            		
		           	 	item.write(file);
		           	 	
		           	 	String status = uploadFileToCKAN(request, file, fileDescription);
		           	 	
		           	 	JSONObject obj = new JSONObject();
		           	 	if (status.contains("201")) {
		           	 		obj.put("name", item.getName());
			                obj.put("size", item.getSize());
			                obj.put("url", filePath + item.getName());
			                //obj.put("thumbnailUrl", filepath + item.getName());
			                obj.put("deleteUrl", filePath + item.getName());
			                obj.put("deleteType", "DELETE");
		           	 	}
		           	 	else {
		           	 		obj.put("error", status);
		           	 	}
		                list.add(obj);
		            }
	            	if (file.exists()) {
	            		//file.delete();
	            	}
	            }
	        }
	        finalObj.put("files", list);
	        
	    } catch (FileUploadException e) {
	    	throw new RuntimeException(e);
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    } finally {
	    	writer.write(finalObj.toString());
	        writer.close();
	    }
	}*/

	String uploadFileToCKAN (HttpServletRequest request, File file, String description) {
		String status = "";
		String sessionKey = "";
		if (Utility.getSessionVariable(request, "sessionKey") != null) {
			sessionKey = Utility.getSessionVariable(request, "sessionKey").toString();
		}
		
		String datasetId = "";
		if (Utility.getSessionVariable(request, "datasetId") != null) {
			datasetId = Utility.getSessionVariable(request, "datasetId").toString();
		}
		
		File uploadFile = file;
		System.out.println("Uploading file "+file.getName());
		System.out.println("sessionKey: "+sessionKey);
		System.out.println("datasetId: "+datasetId);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost("http://localhost:42042/v1/resources");
		httpPost.addHeader("Authorization", sessionKey);
				
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("name", file.getName());
		builder.addTextBody("format", getMimeType(file));
		builder.addTextBody("description", description);
	    builder.addTextBody("setId", datasetId);
	    //builder.addTextBody("password", "pass");
	    builder.addBinaryBody("file", uploadFile,
	      ContentType.APPLICATION_OCTET_STREAM, "xml");
	 
	    HttpEntity multipart = builder.build();
	    httpPost.setEntity(multipart);
	 
	    CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpPost);
			status = response.getStatusLine().toString();
			System.out.println("status: "+status);
		    
			HttpEntity entity = response.getEntity();
	        System.out.println(entity.toString());
	            //return entity != null ? EntityUtils.toString(entity) : null;
	        
		    httpclient.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return status;
		}
	}
	
	private void uploadFilesToCKAN (HttpServletRequest request, List<FileItem> fileList) {
		//List<FileItem> items = fileList;
		System.out.println("Uploading files.");
		ListIterator<FileItem> it = fileList.listIterator();
		
		if (it.hasNext()) {
			BufferedReader in;
			try {
				do {
					FileItem fileItem = it.next();
					File file = new File(request.getServletContext().getRealPath("/")+"/imgs/", fileItem.getName());
					System.out.println("Uploading file "+fileItem.getName());
					Process p = Runtime.getRuntime().exec("curl -H 'Authorization: c821daf7-9439-4dcd-a203-20a57eafdb66' -F 'file=@"+file.getAbsolutePath()+"'  -F 'name="+file.getName()+"' -F 'format="+getMimeType(file)+"'  -F 'description=Test'  -F 'setId=b12c4a48-70c7-465c-a55e-9ffd295f1f50' http://localhost:42042/v1/resources");
					in = new BufferedReader(
		                    new InputStreamReader(p.getInputStream()));
				}
	            while (((in.readLine()) != null) && (it.hasNext()));
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	
    	 
    private String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
            if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
                mimetype = "image/png";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpg")){
                mimetype = "image/jpg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpeg")){
                mimetype = "image/jpeg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("gif")){
                mimetype = "image/gif";
            }else {
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                mimetype  = mtMap.getContentType(file);
            }
        }
        return mimetype;
    }

    private String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        return suffix;
    }
    
    private void createLogFile(String fileName) {
    	File file =new File(fileName);
		
		//if file doesnt exists, then create it
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    private void appendLogFile(String fileName, String content) {
    	File file =new File(fileName);
		
		//if file doesnt exists, then create it
		if(!file.exists()){
			try {
				file.createNewFile();
				FileWriter fileWritter = new FileWriter(file.getName(),true);
		        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		        bufferWritter.write(content);
		        bufferWritter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
