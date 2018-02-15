# myFTP
Simulating FTP server with Java program

Contributors:
1. Akshay Mendki
2. Omkar Acharya

This command simulates basic commands of FTP using Java Sockets. Following commands have been simulated.

1. pwd:           Gives current directory path of Server
2. mkdir dirName: Creates new directory with name directoryName
3. get fileName:  Downloads file present in the Server to the Client. fileName can include the entire Absolute path too.
4. put fileName:  Uploads file present in the Client to the Server. fileName can include the entire absolute path too.
5. cd dirName:    Sets the current directory to the directoryName mentioned in the command. dirName can include the entire absolute path.
6. cd ..  :       Sets the current directory to the parent directory.
7. ls:            Lists the entire files and folders present in current directory.
8. delete file :  Deletes the file present in the current directory. FileName can has path name too, in that case file present in the path will be deleted.
9. quit :         Closes the connection.


How to compile and run?

1. The entire code is divided into three Java files: myFtpServer.java and myFtpServerProcess.java are needed to run Server. myFtpClient.java is needed to run the Client.
2. Copy myFtpServer.java and myFtpServerProcess.java where you want to run the Server.
3. Copy myFtpClient.java in the folder where you want to run the Client.
4. Compile .java files with the command javac fileName.java
5. Run java files after compilation with the command java fileName
6. First compile myFtpServerProcess and then myFtpServer. Or you can compile both together using 
    javac  myFtpServer.java myFtpServerProcess.java
7. After compilation execute the Server using java myFtpServer portNumber. Use any port number that is available to use. Server will listen to the port number mentioned in the command line arguments.
8. Once Server has started, run the Client using java myFtpClient machine_name portnumber (where machine_name and portnumber are command-line parameters). Machine name is the name of machine where server is executed and port number is the port that Server is listening to.
9. Once Client and Server are connected you can try any commands mentioned above. Connection will close if quit is sent by the client.

Folder Structure:

--Client
    --myFtpClient.java
--Server
    --myFtpServer.java
    --myFtpServerProcess.java
--readMe.md
    
This project was done in its entirety by Akshay Mendki and Omkar Acharya. We hereby state that we have not received unauthorized help of any form.
