import java.util.*;
import java.net.*;
import java.io.*;

class myFtpClient {

	// Method to take input from Console
	public static String takeInput() throws Exception {
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(reader);
		return buffer.readLine();
	}

	public static void main(String[] args) throws Exception {

		String machineName = args[0];
		String portNo = args[1];
		int portNumber = Integer.parseInt(portNo);

		// Created a Socket with port number 9999
		Socket clientSocket = new Socket(machineName, portNumber);

		// Initialize DataInputStream and DataOutputStream
		DataInputStream input = new DataInputStream(clientSocket.getInputStream());
		DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
		
		while (true) {
			System.out.print("mytftp> ");
			String command = takeInput();
			output.writeUTF(command);

			// Close the streams if quit
			if (command.equalsIgnoreCase("quit")) {
				input.close();
				output.close();
				break;
			}
			
			if (command.contains("get")) {
				String filePath = "";
				String fileName = "";

				// Check if input has path name. Extract file name from it.
				if (command.contains("/")) {
					filePath = command.split(" ")[1];
					String[] pathArray = filePath.split("/");
					fileName = pathArray[pathArray.length - 1];
				} 
				
				else {
					fileName = command.split(" ")[1];
				}
				// create blank file
				FileOutputStream fileoutput = new FileOutputStream(fileName);
				int characters;
				
				// Write characters coming in from inputStream
				do {
					characters = Integer.parseInt(input.readUTF());
					if (characters != -1) {
						fileoutput.write(characters);
					}
				} while (characters != -1);
				
				fileoutput.close();
				System.out.println("File is received");
			}
			
			else {
				if (command.contains("put")) {
					String fileName = command.split(" ")[1];

					File file = new File(fileName);
					FileInputStream myFile = new FileInputStream(file.getAbsolutePath());
					int characters;
					
					// Send characters to getOutputStream
					do {
						characters = myFile.read();
						output.writeUTF(String.valueOf(characters));
					} while (characters != -1);
					
					myFile.close();
					System.out.println("File is sent");
				}
				
				else {
					// for any other commands than get put quit; simply send the
					// command to the Server
					String inputString = "";
					inputString = input.readUTF();
					System.out.println(inputString);
				}
			}
		}
	}
}
