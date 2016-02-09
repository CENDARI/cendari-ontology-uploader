# cendari-ontology-uploader
Tool mainly designed for uploading ontology files to Cendari repository. While uploading ontology files, it also generates a metadata file describing the relationship between the uploaded files and the metadata file will be stored with other files. In addition, it also allows user to browser and download ontology and metadata files from the Cendari repository. And while browsing the files, it also allows user to upload more files to the server and the metadata file stored on the server will also be updated automatically.

The project can be built with Maven by using:

mvn package

The code will be built into a .war file which can be deployed in Tomcat.
