<!DOCTYPE HTML>
<!--
/*
 * jQuery File Upload Plugin Demo 9.1.0
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */
-->
<%@ page import="java.io.*, javax.servlet.*, javax.servlet.http.*, org.cendari.ontology.upload.*, org.cendari.ontology.download.*, java.util.*, org.cendari.ontology.utility.Utility, org.json.simple.JSONArray, org.json.simple.JSONObject" %>
<html lang="en">
<head>
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<title>CENDARI Ontology</title>
<meta name="description" content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for jQuery. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap styles -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<!-- Generic page styles -->
<link rel="stylesheet" href="css/style.css">
<!-- blueimp Gallery styles -->
<link rel="stylesheet" href="//blueimp.github.io/Gallery/css/blueimp-gallery.min.css">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="css/jquery.fileupload.css">
<link rel="stylesheet" href="css/jquery.fileupload-ui.css">
<link rel="stylesheet" href="css/bootstrap-nav-wizard.css">
<!-- CSS adjustments for browsers with JavaScript disabled -->
<noscript><link rel="stylesheet" href="css/jquery.fileupload-noscript.css"></noscript>
<noscript><link rel="stylesheet" href="css/jquery.fileupload-ui-noscript.css"></noscript>
</head>
<body>
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<strong><a href="index.jsp" class="navbar-brand">CENDARI Ontology</a></strong>
				<button class="navbar-toggle" type="button" data-toggle="collapse"
					data-target="#navbar-main">
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</button>
			</div>
			<div class="navbar-collapse collapse" id="navbar-main">
				<ul class="nav navbar-nav">
					<!-- <li><a href="index.jsp">Home</a></li> -->
					<!-- <li><a href="">About</a></li>
					<li><a href="">Help</a></li> -->
				</ul>

				<ul class="nav navbar-nav navbar-right">
					<!-- <li><a href="">Login</a></li>  -->
					<% 
						if (session.getAttribute("username") != null && session.getAttribute("sysadmin") != null && session.getAttribute("sessionKey") != null && session.getAttribute("host") != null) { 
							out.write("<li><a href=\"https://"+session.getAttribute("host")+"/Shibboleth.sso/Logout\">Logout</a></li>");
						}
						else {
							out.write("<li><a href=\"https://"+request.getHeader("host")+"/Shibboleth.sso/Login?target=https://"+request.getHeader("host")+"/ontologyuploader/index.jsp\">Login</a></li>");
						}
					%>
				</ul>

			</div>
		</div>
	</div>
	<div class="container">
		<br>
		<ul class="breadcrumb" style="background-color:white">
		  <li><a href="listdataspaces.jsp">All Dataspaces</a></li>
		  <li><a href="listdatasets.jsp"><%=session.getAttribute("dataspaceTitle") %></a></li>
		  <li class="active"><%=session.getAttribute("datasetTitle") %></li>
		</ul>
		<hr>
    <!-- <h1>CENDARI Ontology Ingestion</h1>
    <hr>  -->
    <!-- <h2 class="lead">CENDARI Ontology Ingestion</h2> -->
    
	<!-- 
	<ul class="nav nav-wizard"">
		<li class="active"><a href="#ontologydescription">Ontology Description</a></li>
		<li class="disabled"><a>Original Files</a></li>
		<li class="disabled"><a>Transformed Files</a></li>
		<li class="disabled"><a>Additional Files</a></li>
		<li class="disabled"><a>Completion</a></li>
	</ul>
	<br>
	<hr>
	<br>
	<br>
	-->
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade active in" id="ontologydescription">
			<table class="table table-striped table-hover ">
				
			  	<tbody>
			  		<%	
			  			
				  		JSONArray resourceArray = null; 
			        	if (session.getAttribute("sessionKey") != null) {
			        		resourceArray = Utility.getResourcesMetadata(request, session.getAttribute("sessionKey").toString(), session.getAttribute("resources").toString());
			        	}
			  			
						if (resourceArray == null) {
							out.println("<h3>No resource in this dataset.</h3>");
						}
						else {
							out.println("<thread>");
							out.println("<tr>");
							out.println("<th class=\"col-md-1\"></th>");
							out.println("<th class=\"col-md-1\">&nbsp;&nbsp;Resource Name</th>");
							out.println("<th class=\"col-md-2\">Description</th>");
							out.println("<th class=\"col-md-2\">Format</th>");
							out.println("<th class=\"col-md-1\">Mimetype</th>");
							out.println("<th class=\"col-md-2\">Created</th>");
							out.println("<th class=\"col-md-2\">Modified</th>");
							out.println("<th class=\"col-md-1\"></th>");
							out.println("</tr>");
							out.println("</thread>");
							
							int resourceCount = 1;
							for (int count = 0; count < resourceArray.size(); count++) {
						    	JSONObject resource = (JSONObject)resourceArray.get(count);
						    	if (!resource.get("state").equals("deleted")) {
			        %>
					<tr>
			      		<td><%=resourceCount%></td>
			      		</td>
						<td>
							<a href="<%=resource.get("viewDataUrl")%>/download/<%=resource.get("name")%>"><%=resource.get("name")%></a>
						</td>
			      		<td><%=resource.get("description") %></td>
			      		<td><%=resource.get("format") %></td>
			      		<td><%=resource.get("mimetype") %></td>
			      		<td><%=resource.get("created") %></td>
			      		<td><%=resource.get("modified") %></td>
			      		<td>
							<form action="DeleteFile.do" method="POST">
								<input type="hidden" name="resourcePage" value="<%=resource.get("viewDataUrl")%>">
								<input type="hidden" name="fileDeleteUrl" value="<%=resource.get("url")%>">
								<input type="hidden" name="fileName" value="<%=resource.get("name")%>">
								<button type="submit" class="btn btn-danger delete">
									<i class="glyphicon glyphicon-trash"></i>
									<span>Delete</span>
								</button>
								<!-- <button type="submit" class="btn btn-danger">Delete</button> -->
							</form>
						</td>
			      	</tr>
					<%	 
						    		resourceCount++;
						    	}
							}
						}
			  		%>
			  	</tbody>
			</table>	
			<hr>
			<div class="tab-pane fade active in" id="originalfiles">
				<br>
				
				<form id= "fileupload" class="fileupload" action="RecvOriginalFiles.do" method="POST" enctype="multipart/form-data">
					<!-- Redirect browsers with JavaScript disabled to the origin page -->
					<!--<noscript><input type="hidden" name="redirect" value="https://blueimp.github.io/jQuery-File-Upload/"></noscript>-->
					<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
					
					<div class="row fileupload-buttonbar">
						<div class="col-lg-7">
							<p class="lead">Upload more original file(s)</p>
							<!-- The fileinput-button span is used to style the file input field as button -->
							<span class="btn btn-success fileinput-button">
								<i class="glyphicon glyphicon-plus"></i>
								<span>Add files...</span>
								<input type="file" name="files[]" multiple>
							</span>
						</div>
						<!-- The global progress state -->
						<div class="col-lg-5 fileupload-progress fade">
							<!-- The global progress bar -->
							<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
								<div class="progress-bar progress-bar-success" style="width:0%;"></div>
							</div>
							<!-- The extended global progress state -->
							<div class="progress-extended">&nbsp;</div>
						</div>
					</div>
					<!-- The table listing the files available for upload/download -->
					<table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>
				</form>
				
				<form id= "fileupload" class="fileupload" action="RecvTransformedFiles.do" method="POST" enctype="multipart/form-data">
					<!-- Redirect browsers with JavaScript disabled to the origin page -->
					<!--<noscript><input type="hidden" name="redirect" value="https://blueimp.github.io/jQuery-File-Upload/"></noscript>-->
					<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
					
					<div class="row fileupload-buttonbar">
						<div class="col-lg-7">
							<p class="lead">Upload more transformed file(s)</p>
							<!-- The fileinput-button span is used to style the file input field as button -->
							<span class="btn btn-success fileinput-button">
								<i class="glyphicon glyphicon-plus"></i>
								<span>Add files...</span>
								<input type="file" name="files[]" multiple>
							</span>
						</div>
						<!-- The global progress state -->
						<div class="col-lg-5 fileupload-progress fade">
							<!-- The global progress bar -->
							<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
								<div class="progress-bar progress-bar-success" style="width:0%;"></div>
							</div>
							<!-- The extended global progress state -->
							<div class="progress-extended">&nbsp;</div>
						</div>
					</div>
					<!-- The table listing the files available for upload/download -->
					<table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>
				</form>
			
				<form id= "fileupload" class="fileupload" action="RecvAdditionalFiles.do" method="POST" enctype="multipart/form-data">
					<!-- Redirect browsers with JavaScript disabled to the origin page -->
					<!--<noscript><input type="hidden" name="redirect" value="https://blueimp.github.io/jQuery-File-Upload/"></noscript>-->
					<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
					
					<div class="row fileupload-buttonbar">
						<div class="col-lg-7">
							<p class="lead">Upload more additional file(s)</p>
							<!-- The fileinput-button span is used to style the file input field as button -->
							<span class="btn btn-success fileinput-button">
								<i class="glyphicon glyphicon-plus"></i>
								<span>Add files...</span>
								<input type="file" name="files[]" multiple>
							</span>
						</div>
						<!-- The global progress state -->
						<div class="col-lg-5 fileupload-progress fade">
							<!-- The global progress bar -->
							<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
								<div class="progress-bar progress-bar-success" style="width:0%;"></div>
							</div>
							<!-- The extended global progress state -->
							<div class="progress-extended">&nbsp;</div>
						</div>
					</div>
					<!-- The table listing the files available for upload/download -->
					<table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>
				</form>
				
				<form action="RecvAllFiles.do" method="POST">
					<br>
					<br>
					<br>
					<br>
					<div class="col-sm-12">
						<input type="hidden" name="nextPage" value="finished">
						<button type="submit" class="btn btn-info pull-right">Generate/Update Relationship File >></button>
					</div>
					<br>
					<br>
					<br>
					</p>
				</form>
			</div> 
			
			
			
			
		</div>
		<br>
		
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Notes</h3>
			</div>
			<div class="panel-body">
				<ul>
					<li>After uploading more original/transformed/additional file(s), please press "Generate/Update Relationship File" to generate/update the relationship file.</li>
	            	<!-- 
	                <li>The maximum file size for uploads in this demo is <strong>999 KB</strong> (default file size is unlimited).</li>
	                <li>Only image files (<strong>JPG, GIF, PNG</strong>) are allowed in this demo (by default there is no file type restriction).</li>
	                <li>Uploaded files will be deleted automatically after <strong>5 minutes or less</strong> (demo files are stored in memory).</li>
	                <li>You can <strong>drag &amp; drop</strong> files from your desktop on this webpage (see <a href="https://github.com/blueimp/jQuery-File-Upload/wiki/Browser-support">Browser support</a>).</li>
	                <li>Please refer to the <a href="https://github.com/blueimp/jQuery-File-Upload">project website</a> and <a href="https://github.com/blueimp/jQuery-File-Upload/wiki">documentation</a> for more information.</li>
	                <li>Built with the <a href="http://getbootstrap.com/">Bootstrap</a> CSS framework and Icons from <a href="http://glyphicons.com/">Glyphicons</a>.</li>
	            	 -->
				</ul>
			</div>
		</div>
	</div>
	<!-- The blueimp Gallery widget -->
	<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
		<div class="slides"></div>
		<h3 class="title"></h3>
		<a class="prev">‹</a>
		<a class="next">›</a>
		<a class="close">×</a>
		<a class="play-pause"></a>
		<ol class="indicator"></ol>
	</div>
	<!-- The template to display files available for upload -->
	<script id="template-upload" type="text/x-tmpl">
	{% for (var i=0, file; file=o.files[i]; i++) { %}
		<tr class="template-upload fade">
			<td>
				<span class="preview"></span>
			</td>
			<td>
				<p class="name">{%=file.name%}</p>
				<strong class="error text-danger"></strong>
			</td>
			<td>
				<p class="size">Processing...</p>
				<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
			</td>
			<td>
				{% if (!i && !o.options.autoUpload) { %}
					<button class="btn btn-primary start" disabled>
						<i class="glyphicon glyphicon-upload"></i>
						<span>Upload</span>
					</button>
				{% } %}
				{% if (!i) { %}
					<button class="btn btn-warning cancel">
						<i class="glyphicon glyphicon-ban-circle"></i>
						<span>Cancel</span>
					</button>
				{% } %}
			</td>
		</tr>
	{% } %}
	</script>
	<!-- The template to display files available for download -->
	<script id="template-download" type="text/x-tmpl">
	{% for (var i=0, file; file=o.files[i]; i++) { %}
		<tr class="template-download fade">
			<td>
				<span class="preview">
					{% if (file.thumbnailUrl) { %}
						<a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
					{% } %}
				</span>
			</td>
			<td>
				<p class="name">
					{% if (file.url) { %}
						<a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
					{% } else { %}
						<span>{%=file.name%}</span>
					{% } %}
				</p>
				{% if (file.error) { %}
					<div><span class="label label-danger">Error</span> {%=file.error%}</div>
				{% } %}
			</td>
			<td>
				<span class="size pull-right">{%=o.formatFileSize(file.size)%}</span>
			</td>
			<td>
				{% if (file.deleteUrl) { %}
					<!--<button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
						<i class="glyphicon glyphicon-trash"></i>
						<span>Delete</span>
					</button>-->
					<!--<input type="checkbox" name="delete" value="1" class="toggle">-->
				{% } else { %}
					<button class="btn btn-warning cancel">
						<i class="glyphicon glyphicon-ban-circle"></i>
						<span>Cancel</span>
					</button>
				{% } %}
			</td>
		</tr>
	{% } %}
	</script>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
	<script src="js/vendor/jquery.ui.widget.js"></script>
	<!-- The Templates plugin is included to render the upload/download listings -->
	<script src="//blueimp.github.io/JavaScript-Templates/js/tmpl.min.js"></script>
	<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
	<script src="//blueimp.github.io/JavaScript-Load-Image/js/load-image.all.min.js"></script>
	<!-- The Canvas to Blob plugin is included for image resizing functionality -->
	<script src="//blueimp.github.io/JavaScript-Canvas-to-Blob/js/canvas-to-blob.min.js"></script>
	<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<!-- blueimp Gallery script -->
	<script src="//blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script>
	<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
	<script src="js/jquery.iframe-transport.js"></script>
	<!-- The basic File Upload plugin -->
	<script src="js/jquery.fileupload.js"></script>
	<!-- The File Upload processing plugin -->
	<script src="js/jquery.fileupload-process.js"></script>
	<!-- The File Upload image preview & resize plugin -->
	<script src="js/jquery.fileupload-image.js"></script>
	<!-- The File Upload audio preview plugin -->
	<script src="js/jquery.fileupload-audio.js"></script>
	<!-- The File Upload video preview plugin -->
	<script src="js/jquery.fileupload-video.js"></script>
	<!-- The File Upload validation plugin -->
	<script src="js/jquery.fileupload-validate.js"></script>
	<!-- The File Upload user interface plugin -->
	<script src="js/jquery.fileupload-ui.js"></script>
	<!-- The main application script -->
	<script src="js/main.js"></script>
	<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
	<!--[if (gte IE 8)&(lt IE 10)]>
	<script src="js/cors/jquery.xdr-transport.js"></script>
	<![endif]-->
</body>
</html>
