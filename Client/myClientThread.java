import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class myClientThread extends Thread {
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String command = "";
	
	myClientThread(Socket socket){
		this.socket = socket;
	}
	
	public void sendDataToServer(String mycommand){
		try {
			command = mycommand;
			
			if(command.contains("put")){
				executePut();
			}
			else
				output.writeUTF(command);
				output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			while(true){
				
				while(input.available() == 0){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if (command.equalsIgnoreCase("quit")) {
					input.close();
					output.close();
					socket.close();
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
						// for any other commands than get put quit; simply send the
						// command to the Server
						String inputString = "";
						inputString = input.readUTF();
						System.out.println(inputString);
					}				
			
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void executePut(){
		try{
			
			String fileName = command.split(" ")[1];

			File file = new File(fileName);
			FileInputStream myFile = new FileInputStream(file.getAbsolutePath());
			int characters;

			// Send characters to getOutputStream
			do {
				characters = myFile.read();
				output.writeUTF(String.valueOf(characters));
			} while (characters != -1);
			output.flush();
			myFile.close();
			System.out.println("File is sent");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}