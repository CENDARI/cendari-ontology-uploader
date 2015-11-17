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
<%@ page import="java.util.Map, java.util.HashMap, java.util.Enumeration, org.cendari.ontology.utility.Utility, org.json.simple.JSONArray, org.json.simple.JSONObject" %>
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

<script src="js/gen_validatorv4.js" type="text/javascript"></script>
<script>
function validateForm() {
    var datasetTitle = document.forms["datasetDescription"]["datasetTitle"].value;
    if (datasetTitle == null || datasetTitle == "") {
        alert("Please enter the dataset title.");
        document.forms["datasetDescription"]["datasetTitle"].focus();
        return false;
    }
    /*if (datasetTitle.length < 2 || datasetTitle.length > 100) {
    	alert("The length of the dataset title should be between 2 and 100 characters long.");
        document.forms["datasetDescription"]["datasetTitle"].focus();
        return false; 
    }
    var reg = new RegExp("^[a-z_-]{2,100}$");
    if (!reg.test(datasetTitle)) {
    	alert("The dataset title contains only lowercase alphanumeric characters, - and _");
        document.forms["datasetDescription"]["datasetTitle"].focus();
        return false;
    }*/
    
}

</script>

</head>
<body>
	<%
		if (session.getAttribute("alertMessage") != null) {
			System.out.println("alertMessage");
			String alertMessage = (String) session.getAttribute("alertMessage");
			out.write("<script>");
			out.write("alert(\"" + alertMessage + "\")");
			out.write("</script>");
			session.removeAttribute("alertMessage");
		}
		if (session.getAttribute("datasetTitleWarning") != null) {
			String warningMessage = (String) session.getAttribute("datasetTitleWarning");
			out.write("<script>");
			out.write("if (window.confirm('"+warningMessage+"')) {");
			out.write("    window.location.href = \"RecvDatasetDescriptionIgnoreWarning.do\"");
			out.write("} else {");
			out.write("    window.location.href = \"uploaddatasetdescription.jsp\"");
			out.write("}");
			out.write("</script>");
			session.removeAttribute("datasetTitleWarning");
		}
	%>
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
					<!-- <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#" id="themes">Themes <span class="caret"></span></a>
						<ul class="dropdown-menu" aria-labelledby="themes">
							<li><a href="../default/">Default</a></li>
							<li class="divider"></li>
							<li><a href="../cerulean/">Cerulean</a></li>
							<li><a href="../cosmo/">Cosmo</a></li>
							<li><a href="../cyborg/">Cyborg</a></li>
							<li><a href="../darkly/">Darkly</a></li>
							<li><a href="../flatly/">Flatly</a></li>
							<li><a href="../journal/">Journal</a></li>
							<li><a href="../lumen/">Lumen</a></li>
							<li><a href="../paper/">Paper</a></li>
							<li><a href="../readable/">Readable</a></li>
							<li><a href="../sandstone/">Sandstone</a></li>
							<li><a href="../simplex/">Simplex</a></li>
							<li><a href="../slate/">Slate</a></li>
							<li><a href="../spacelab/">Spacelab</a></li>
							<li><a href="../superhero/">Superhero</a></li>
							<li><a href="../united/">United</a></li>
							<li><a href="../yeti/">Yeti</a></li>
						</ul>
					</li>  -->
					<!-- <li><a href="index.jsp">Home</a></li> -->
					<!-- <li><a href="">About</a></li>
					<li><a href="">Help</a></li> -->
					<!-- <li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#" id="download">Flatly <span
							class="caret"></span></a>
						<ul class="dropdown-menu" aria-labelledby="download">
							<li><a href="http://jsfiddle.net/bootswatch/jmg3gykg/">Open
									Sandbox</a></li>
							<li class="divider"></li>
							<li><a href="./bootstrap.min.css">bootstrap.min.css</a></li>
							<li><a href="./bootstrap.css">bootstrap.css</a></li>
							<li class="divider"></li>
							<li><a href="./variables.less">variables.less</a></li>
							<li><a href="./bootswatch.less">bootswatch.less</a></li>
							<li class="divider"></li>
							<li><a href="./_variables.scss">_variables.scss</a></li>
							<li><a href="./_bootswatch.scss">_bootswatch.scss</a></li>
						</ul></li>  -->
				</ul>

				<ul class="nav navbar-nav navbar-right">
					<!-- <li><a href="">Login</a></li>  -->
					<% 
						if (session.getAttribute("sessionKey") != null) { 
							out.write("<li><a href=\"https://localhost/Shibboleth.sso/Logout\">Logout</a></li>");
						}
						else {
							out.write("<li><a href=\"https://localhost/Shibboleth.sso/Login?target=https://localhost/cendariontology/index.jsp\">Login</a></li>");
						}
					%>
				</ul>

			</div>
		</div>
	</div>
	<br>
	<br>
	<br>
	<div class="container">
    <!-- <h1>CENDARI Ontology Ingestion</h1>
    <hr>  -->
    <!-- <h2 class="lead">CENDARI Ontology Ingestion</h2> -->
    
	<ul class="nav nav-wizard"">
		<!-- <li class="active"><a href="#ontologydescription" data-toggle="tab" aria-expanded="true">Ontology Description</a></li>
		<li class="disabled"><a href="#originalfiles" data-toggle="tab" aria-expanded="true">Original Files</a></li>
		<li class="disabled"><a href="#transformedfiles" data-toggle="tab" aria-expanded="true">Transformed Files</a></li>
		<li class="disabled"><a href="#additionalfiles" data-toggle="tab" aria-expanded="true">Additional Files</a></li>  -->
		
		<li class="active"><a href="">Dataset Description</a></li>
		<li class="disabled"><a>Original Files</a></li>
		<li class="disabled"><a>Transformed Files</a></li>
		<li class="disabled"><a>Additional Files</a></li>
		<li class="disabled"><a>Completion</a></li>
	</ul>
	<br>
	<hr>
	<br>
	<br>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade active in" id="datasetdescription">
				
			<form class="form-horizontal" name="datasetDescription" onsubmit="return validateForm()" action="RecvDatasetDescription.do" method="POST">
			
				<fieldset>
					<!--<legend>Legend</legend>-->
					<div class="form-group">
						<label for="inputDefault" class="col-lg-2 control-label">Dataset Title:</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" id="datasetTitle" name="datasetTitle" placeholder="e.g. A descriptive title">
							<!-- <label style="color: #FF0000;">${datasetTitleErrorMessage}</label> -->
						</div>
					</div>
					<br>
					<div class="form-group">
						<label for="textArea" class="col-lg-2 control-label">Description (Optional):</label>
						<div class="col-lg-10">
							<textarea class="form-control" rows="3" id="datasetDescription" name="datasetDescription" placeholder="Some useful notes about the dataset"></textarea>
							<!--<span class="help-block">A longer block of help text that breaks onto a new line and may extend beyond one line.</span>-->
						</div>
					</div>
					<br>
					<div class="form-group">
				      <label for="select" class="col-lg-2 control-label">Dataspace ID</label>
				      <div class="col-lg-5">
				        <select class="form-control" id="dataspaceId" name="dataspaceId">
				        	<%
					        	JSONArray dataspaceArray = null; 
					        	if (session.getAttribute("sessionKey") != null) {
					        		dataspaceArray = Utility.getDataspaceNames(request, session.getAttribute("sessionKey").toString(), "http://localhost:42042/v1/dataspaces");
					        	}
					        	if (dataspaceArray != null) {
									for (int count = 0; count < dataspaceArray.size(); count++) {
								    	JSONObject dataspace = (JSONObject)dataspaceArray.get(count);
								    	if (dataspace.get("title").equals("CENDARI Ontologies")) {
								    		out.write("<option selected value=\""+dataspace.get("id")+"\">"+dataspace.get("title")+"</option>");
								    	}
								    	else {
								    		out.write("<option value=\""+dataspace.get("id")+"\">"+dataspace.get("title")+"</option>");
								    	}
									}
								}
				        	%>
				        </select> 
				      </div>
				    </div>
					<br>
					<!-- <div class="form-group">
					  <label for="inputDefault" class="col-lg-2 control-label">API Key (Optional):</label>
					  <div class="col-lg-10">
						<input type="text" class="form-control" id="authorization" name="authorization" placeholder="The API key of you CKAN account.">
					  </div>
					</div> -->
					<br>
					<div class="form-group">
						<div class="col-sm-12">
							<input type="hidden" name="nextPage" value="uploadoriginalfiles">
							<button type="submit" class="btn btn-info pull-right">Next Step >></button>
						</div>
					</div>
				
					<!--<div class="form-group">
					  <div class="col-lg-12 col-lg-offset-9">
						<button type="reset" class="btn btn-default">Cancel</button>
						<button type="submit" class="btn btn-primary">Next Step >></button>
					  </div>
					</div>-->
				</fieldset>
			</form>
			<!-- <script type="text/javascript">
				var formValidator  = new Validator("datasetDescription");
				formValidator.addValidation("datasetTitle", "req", "Please enter the dataset title.");
				formValidator.addValidation("datasetTitle", "minlen=2", "The minimum length of the dataset title is 2.");
				formValidator.addValidation("datasetTitle", "maxlen=100", "The maximum length of the dataset title is 100.");
				formValidator.addValidation("datasetTitle", "regexp=[a-z_-]{1,100}$", "The dataset title can only contain lowercase alphanumeric characters, - and _");
				function validateCharactersCase() {
					var form = document.forms["datasetDescription"];
				  	if (("([a-z]{1,100})$").test(form.datasetDescription.value)) {
				  		return true;
					}
				  	else {
					  	sfm_show_error_msg('Hello');
					  	return false;
				  	}
				}
				formvalidator.setAddnlValidationFunction("validateCharactersCase");
				
			</script> -->
		</div>	
	</div>
	
	<hr>
	
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Notes</h3>
        </div>
        <div class="panel-body">
            <ul>
            	<li>Dataset Title is a required field.</li>
            	<li>Description is optional.</li>
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
                    <span>Start</span>
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
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>Delete</span>
                </button>
                <input type="checkbox" name="delete" value="1" class="toggle">
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
