# Cendari Ontology Uploader
This web application is mainly designed for uploading ontology files to Cendari repository. While uploading ontology files, it also generates a metadata file describing the relationship between the uploaded files and the metadata file will be stored with other files. In addition, it also allows user to browser and download ontology and metadata files from the Cendari repository. And while browsing the files, it also allows user to upload more files to the server and the metadata file stored on the server will also be updated automatically.

# Requirements
1. Java Development Kit, Java 7 or later
2. A web server to host the web application, e.g. Tomcat 7.
3. A build automation tool to build the project, e.g. Maven 3.


# How to use
The project can be built with a build automation tool. For example, it can be built by Maven with the command:

mvn package

After this, the code will be built into a .war file, which can then be deployed in the web server.
