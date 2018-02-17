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

		// create object of ServerProcess that executes the command
		myFtpServerProcess mycommand = new myFtpServerProcess();

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
			if (splitCommand(inputString)[0].equalsIgnoreCase("get"))
			{
				mycommand.get(output, inputString);
			}

			if (splitCommand(inputString)[0].equalsIgnoreCase("put"))
			{
				mycommand.put(input, inputString);
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
