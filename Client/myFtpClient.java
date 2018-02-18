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

	public static void main(String[] args) throws Exception {

		/*String machineName = args[0];
		String nportNo = args[1];
		String tportNo = args[2];
		int nportNumber = Integer.parseInt(nportNo);
		int tportNumber = Integer.parseInt(tportNo);*/

		// Created a Socket with port number 9999
		//Socket clientSocket = new Socket(machineName, nportNumber);
		Socket nclientSocket = new Socket("localhost", 9999);
		//Socket tclientSocket = new Socket("localhost", 9998);
		
		myClientThread myClientThread = new myClientThread(nclientSocket);
		myClientThread.start();
		while (true) {
			Thread.sleep(510);
			String command = takeInput();
			
			myClientThread.sendDataToServer(command);	
			
					
			if (command.equalsIgnoreCase("quit")) {
				nclientSocket.close();
				break;
			}			
		}
	}
}
