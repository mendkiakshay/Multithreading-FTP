
import java.net.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

class myFtpServer {

    public static void main(String[] args) {
        //String nport = args[0];
        //int nport = Integer.parseInt(portNo);
        //String tport = args[1];
        //int tport = Integer.parseInt(portNo);

        // create object of ServerProcess that executes the command
        System.out.println("Server Started");

        try {
            // Create nport and tport sockets
            ServerSocket nserSocket = new ServerSocket(9999);
            ServerSocket tserSocket = new ServerSocket(9998);
            while (true) {
//                Socket nsocket = nserSocket.accept();
//                Socket tsocket = nserSocket.accept();
                myServerThread myNServerThread = new myServerThread(nserSocket);
                myNServerThread.start();
                myServerThread myTServerThread = new myServerThread(tserSocket);
                myTServerThread.start();
            }
        } catch (Exception ex) {
            System.out.println("exceptionnn" + ex + " exception " + ex.getMessage());
        }
    }
}
