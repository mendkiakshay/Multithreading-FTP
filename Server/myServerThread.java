package GitSynFiles;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class myServerThread extends Thread {
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	myFtpServerProcess mycommand = new myFtpServerProcess();
	String inputString = null;
	
	myServerThread(Socket socket){
		this.socket = socket;
	}
	
	public String[] splitCommand(String command) {
		return command.split(" ");
	}
	
	public void run(){
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
			while (true) {
				
				while(input.available() == 0){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				// read the command
				inputString = input.readUTF();

				// Call respective methods of ServerProcess of the FTP commands
				// received from Client
				if (splitCommand(inputString)[0].equalsIgnoreCase("mkdir")) {
					output.writeUTF(mycommand.mkdir(splitCommand(inputString)[1]));
					output.flush();
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("cd")) {
					if (!splitCommand(inputString)[1].equalsIgnoreCase("..")) {
						output.writeUTF(mycommand.setCurrent(splitCommand(inputString)[1]));
						output.flush();
					} 
					
					else {
						output.writeUTF(mycommand.setPrevious());
						output.flush();
					}
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("delete")) {
					output.writeUTF(mycommand.delete(splitCommand(inputString)[1]));
					output.flush();
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("ls")) {

					File[] files;
					String allPath = "";
					if (splitCommand(inputString).length == 1) {
						files = mycommand.ls();

						for (File file : files) {
							allPath = allPath + "  " + file.getName() + '\t';
						}
					} 
					
					else {
						files = mycommand.ls(new File(splitCommand(inputString)[1]));
						for (File file : files) {
							allPath = allPath + "  " + file.getName() + '\t';
						}
					}
					output.writeUTF(allPath);
					output.flush();
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("pwd")) {

					output.writeUTF(mycommand.pwd(new File("")));
					output.flush();
				}

				// Get and Put are handled by myftpServer and not
				// myFtpServerProcess
				if (splitCommand(inputString)[0].equalsIgnoreCase("get")) {
					mycommand.get(output, inputString);
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("put")) {
					mycommand.put(input, inputString);
				}

				// close input and output streams
				if (inputString.equalsIgnoreCase("quit")) {
					input.close();
					output.close();
					socket.close();
					break;
				}
			}
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}

