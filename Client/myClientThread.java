import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class myClientThread extends Thread {

    Socket socket,tsocket;

    DataInputStream input,tinput;
    DataOutputStream output,toutput;
    String command = "";
    boolean shouldrun = true;
    String port;

    myClientThread(Socket socket, String port) {
        try {
        	this.port=port;

        	if(this.port=="nport")
        	{
	            this.socket = socket;
	            input = new DataInputStream(socket.getInputStream());
	            output = new DataOutputStream(socket.getOutputStream());
        	}
        	else
        	{
        		this.tsocket = socket;
 	            tinput = new DataInputStream(tsocket.getInputStream());
 	            toutput = new DataOutputStream(tsocket.getOutputStream());
              // start();
        	}
        	}
        catch (IOException ex)
        {
            Logger.getLogger(myClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendDataToServer(String mycommand) {
        try {
            if(port == "nport")
            {
              command = mycommand;
            System.out.println("Inside sendDataToServer Method");
            if (command.contains("put")) {
                //System.outprintln(input.readUTF());
                output.writeUTF(command);
                output.flush();
                System.out.println(input.readUTF());
                executePut();
            } else {
                output.writeUTF(command);
                if(command.contains("get")){
                  System.out.println(input.readUTF());
                }
            }
            output.flush();
          }
          else
          {
            command = mycommand;
            toutput.writeUTF(command);
            toutput.flush();
          }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

            while (shouldrun) {

              /*  if (command.equalsIgnoreCase("quit")) {
                    break;
                }*/

            	 if(input!=null)
                 {
                     if(shouldrun)
                    while ((shouldrun) && (input.available() == 0)) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    	Thread.currentThread().interrupt();
                    }
                    if(!shouldrun)
                    {
                      break;
                    }
                }
                }


                if (command.contains("get"))
                {
                	try {
                	executeGet();
                  }
                	catch(Exception ex)
                	{
                    ex.printStackTrace();
                	System.out.println(ex.getMessage());
                	}

                }
                else
                {
                    if(command!="")
                    {
                    // for any other commands than get put quit; simply send the
                    // command to the Server
                    if(port == "nport")
                    {
                    String inputString = "";
                    if (shouldrun) {
                        inputString = input.readUTF();
                    }
                    System.out.println("output is: "+inputString);
                    }
                    else
                    {
                      String inputString = "";
                      if (shouldrun) {
                          inputString = tinput.readUTF();
                      }
                      System.out.println("output is: "+inputString);
                    }
                  }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  synchronized public void executeGet()
  	{
    	try {
    		String filePath = "";
    		String fileName = "";

    		// Check if input has path name. Extract file name from it.
    		if (command.contains("/")) {
    			filePath = command.split(" ")[1];
    			String[] pathArray = filePath.split("/");
    			fileName = pathArray[pathArray.length - 1];
    		} else {
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
        //System.out.println("ID IS:"+input.readUTF());
    		System.out.println("File is received");

    	}catch(Exception ex)
    	{
        ex.printStackTrace();
    		System.out.println(ex.getMessage());
    	}
    }

	public void executePut() {
        try {

            String fileName = command.split(" ")[1];

            File file = new File(fileName);
            FileInputStream myFile = new FileInputStream(file.getAbsolutePath());
            //System.out.println(file.getAbsolutePath());
            int characters;

            // Send characters to getOutputStream
            do {
                characters = myFile.read();
                output.writeUTF(String.valueOf(characters));
            } while (characters != -1);
            output.flush();
            myFile.close();
            System.out.println("File is sent");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
        	System.out.println("inside void close");
            shouldrun = false;
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
