import java.net.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

class myFtpServer {

	// splitCommand splits the string with space
	public static String[] splitCommand(String command) {
		return command.split(" ");
	}

	public static void main(String[] args) {
		String portNo = args[0];
		int portNumber = Integer.parseInt(portNo);

		// create object of ServerProcess that executes the command
		myFtpServerProcess mycommand = new myFtpServerProcess();
		System.out.println("Server Started");

		try {
			// Create server socket
			ServerSocket serSocket = new ServerSocket(portNumber);
			// accept a new connection
			Socket socket = serSocket.accept();

			// create data input and output streams
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String inputString = null;

			while (true) {
				// read the command
				inputString = input.readUTF();

				// Call respective methods of ServerProcess of the FTP commands
				// received from Client
				if (splitCommand(inputString)[0].equalsIgnoreCase("mkdir")) {
					output.writeUTF(mycommand.mkdir(splitCommand(inputString)[1]));
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("cd")) {
					if (!splitCommand(inputString)[1].equalsIgnoreCase("..")) {
						output.writeUTF(mycommand.setCurrent(splitCommand(inputString)[1]));
					} 
					
					else {
						output.writeUTF(mycommand.setPrevious());
					}
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("delete")) {
					output.writeUTF(mycommand.delete(splitCommand(inputString)[1]));
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
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("pwd")) {

					output.writeUTF(mycommand.pwd(new File("")));
				}

				// Get and Put are handled by myftpServer and not
				// myFtpServerProcess
				if (splitCommand(inputString)[0].equalsIgnoreCase("get")) {
					// take file requested by Client into FileInputStream
					FileInputStream myFile = new FileInputStream(splitCommand(inputString)[1]);
					int characters;
					do {
						// read the characters and write them into files
						characters = myFile.read();
						output.writeUTF(String.valueOf(characters));
					} while (characters != -1);
					
					myFile.close();
				}

				if (splitCommand(inputString)[0].equalsIgnoreCase("put")) {
					String filePath = "";
					String fileName = "";

					// extract file name from file path
					if (splitCommand(inputString)[1].contains("/")) {
						filePath = splitCommand(inputString)[1];
						String[] pathArray = filePath.split("/");
						fileName = pathArray[pathArray.length - 1];
					} 
					
					else {
						fileName = splitCommand(inputString)[1];
					}

					// get current path of Server
					File file = new File(System.getProperty("user.dir"));
					String s = file.getAbsolutePath();

					// create blank file with same name at current path of
					// Server
					FileOutputStream fileoutput = new FileOutputStream(s + "/" + fileName);

					int characters;

					// read characters coming from inputStream from Client
					do {
						characters = Integer.parseInt(input.readUTF());
						if (characters != -1) {
							// Write characters to blank file
							fileoutput.write(characters);
						}
					} while (characters != -1);
					
					fileoutput.close();
					System.out.println("File is Received");
				}

				// close input and output streams
				if (inputString.equalsIgnoreCase("quit")) {
					input.close();
					output.close();
					break;
				}
			}
		} 
		
		catch (Exception ex) {
			System.out.println("exceptionnn" + ex + " exception " + ex.getMessage());
		}
	}
}
