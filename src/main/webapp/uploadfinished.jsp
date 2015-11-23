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
						if (session.getAttribute("username") != null && session.getAttribute("sysadmin") != null && session.getAttribute("sessionKey") != null && session.getAttribute("host") != null) { 
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
		<ul class="nav nav-wizard"">
			<!-- <li class="active"><a href="#ontologydescription" data-toggle="tab" aria-expanded="true">Ontology Description</a></li>
			<li class="disabled"><a href="#originalfiles" data-toggle="tab" aria-expanded="true">Original Files</a></li>
			<li class="disabled"><a href="#transformedfiles" data-toggle="tab" aria-expanded="true">Transformed Files</a></li>
			<li class="disabled"><a href="#additionalfiles" data-toggle="tab" aria-expanded="true">Additional Files</a></li>  -->
			
			<li class="disabled"><a href="#ontologydescription">Dataset Description</a></li>
			<li class="disabled"><a>Original Files</a></li>
			<li class="disabled"><a>Transformed Files</a></li>
			<li class="disabled"><a>Additional Files</a></li>
			<li class="active"><a>Completion</a></li>
		</ul>
		<br>
		<hr>
		<br>
		<br>
		<div class="alert alert-dismissible alert-success">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<strong>All Done.</strong>
			
			
			<!-- <a href="ontologydescription.jsp" class="alert-link">here</a> -->
		</div>
		<div>
			<br>
			<br>
			<br>
			<form action="NextStep" method="GET">
				<input type="hidden" name="nextPage" value="createanotherdataset">
				<button type="submit" class="btn btn-info pull-right">Create another dataset >></button>
			</form>
			<br>
			<br>
			<br>
			<form action="NextStep" method="GET">
				<input type="hidden" name="nextPage" value="listdataspaces">
				<button type="submit" class="btn btn-success pull-right">View dataspaces, datasets and files >></button>
			</form>
			</div>
		</div>

	</div>
	
	
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
