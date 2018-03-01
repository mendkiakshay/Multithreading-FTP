
import java.util.*;
import java.net.*;
import java.io.*;

class myFtpClient {

    // Method to take input from Console
    public static String takeInput() throws Exception {
        System.out.print("mytftp> ");
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(reader);
        return buffer.readLine();
    }

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) throws Exception {

        /*String machineName = args[0];
		String nportNo = args[1];
		String tportNo = args[2];
		int nportNumber = Integer.parseInt(nportNo);
		int tportNumber = Integer.parseInt(tportNo);*/
        // Created a Socket with port number 9999
        //Socket clientSocket = new Socket(machineName, nportNumber);
        Socket nclientSocket = new Socket("localhost", 9999);
        myClientThread myClientThread = new myClientThread(nclientSocket,"nport");
        //myClientThread.command = takeInput();
        myClientThread.start();


        Socket tclientSocket = new Socket("localhost", 9998);
        myClientThread myClientThreadTerminate = new myClientThread(tclientSocket,"tport");

        boolean isAmpersantStarted=false;
        myClientThread myClientThreadamparsent=null;

        while (true) {
            Thread.sleep(510);
            String command = takeInput();

            if(command.contains("&"))
            {
              // myClientThread.command="";
              // if(!isAmpersantStarted)
            	// {
              myClientThreadamparsent = new myClientThread(nclientSocket,"nport");
              myClientThreadamparsent.start();
              // }
              isAmpersantStarted=true;
              myClientThreadamparsent.sendDataToServer(command);
            }else if(!command.contains("terminate"))
            {

            myClientThread.sendDataToServer(command);
            }
            else
            myClientThreadTerminate.sendDataToServer(command);

            if (command.equalsIgnoreCase("quit")) {

            	if(isAmpersantStarted)
                {
              		System.out.println("interrupting thread");
              		myClientThread.interrupt();
              		myClientThreadamparsent.interrupt();
              		myClientThread.close();
              		myClientThreadamparsent.close();
              		break;
               }
            	else
            	{
            		myClientThread.interrupt();
            		myClientThread.close();
            		break;
            	}
            }

        }
    }
}
