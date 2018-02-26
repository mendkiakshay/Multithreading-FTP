import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class myServerThread extends Thread {

    Socket socket;
    DataInputStream input;
    DataOutputStream output;
    myFtpServerProcess mycommand = new myFtpServerProcess();
    String inputString = null;

    myServerThread(ServerSocket sersocket)
    {
        try
        {
            this.socket = sersocket.accept();
            System.out.println("Client connection arrived");
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex)
        {
            Logger.getLogger(myServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String[] splitCommand(String command)
    {
        return command.split(" ");
    }
    CommandLogs createCommandID(String status, String fileName)
    {
      CommandLogs cmd = new CommandLogs(status, fileName);
      //CommandLogs.listOfCommands.add(cmd);

      System.out.println("LIST BEFORE IS: ");
      for(CommandLogs c2 : CommandLogs.listOfCommands)
      {
        System.out.println(c2.commandId+" "+c2.status+" "+c2.fileName);
      }

      return cmd;
    }

    public void run()
    {
        try {
            while (true) {
            while (input.available() == 0)
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

                // read the command
                inputString = input.readUTF();
                System.out.println(inputString);

                // Call respective methods of ServerProcess of the FTP commands
                if (splitCommand(inputString)[0].equalsIgnoreCase("terminate")) {
                    output.writeUTF("Terminate on Server");
                    output.flush();
                }

                // Call respective methods of ServerProcess of the FTP commands
                if (splitCommand(inputString)[0].equalsIgnoreCase("mkdir")) {
                    output.writeUTF(mycommand.mkdir(splitCommand(inputString)[1]));
                    output.flush();
                }
                // received from Client
                if (splitCommand(inputString)[0].equalsIgnoreCase("cd"))
                {
                    if (!splitCommand(inputString)[1].equalsIgnoreCase("..")) {
                        output.writeUTF(mycommand.setCurrent(splitCommand(inputString)[1]));
                        output.flush();
                    }
                     else
                    {
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
                    } else {
                        files = mycommand.ls(new File(splitCommand(inputString)[1]));
                        for (File file : files) {
                            allPath = allPath + "  " + file.getName() + '\t';
                        }
                    }
                    output.writeUTF(allPath);
                    output.flush();
                }

                if (splitCommand(inputString)[0].equalsIgnoreCase("pwd")) {
                	System.out.println("Inside PWD");
                    output.writeUTF(mycommand.pwd(new File("")));
                    output.flush();
                }

                // Get and Put are handled by myftpServer and not
                // myFtpServerProcess
                if (splitCommand(inputString)[0].equalsIgnoreCase("get"))
                {
                  CommandLogs cmd	=	createCommandID("E",inputString);
                  output.writeUTF(Integer.toString(cmd.commandId));
                  output.flush();
                  mycommand.get(output, inputString);

                  int index = -1;
                  CommandLogs commandToChange=null;
                  for(CommandLogs c : CommandLogs.listOfCommands)
                  {
                    if(c.commandId == cmd.commandId)
                    {
                      index = CommandLogs.listOfCommands.indexOf(c);
                      commandToChange=c;
                      break;
                    }
                  }
                  commandToChange.status ="D";
                  CommandLogs.listOfCommands.set(index, commandToChange);
                  System.out.println("LIST AFTER IS: "+CommandLogs.listOfCommands);
                  // System.out.println("LIST BEFORE IS: ");
                  for(CommandLogs c2 : CommandLogs.listOfCommands)
                  {
                    System.out.println(c2.commandId+" "+c2.status+" "+c2.fileName);
                  }
                  // output.writeUTF(cmd.commandId);
                }

                //System.out.println("input string is:"+inputString);
                if (splitCommand(inputString)[0].equalsIgnoreCase("put"))
                {
                    // System.out.println("input string is:"+inputString);
                    CommandLogs cmd	=	createCommandID("E",inputString);
                    output.writeUTF(Integer.toString(cmd.commandId));
                    output.flush();
                    mycommand.put(input, inputString);

                    int index = -1;
                    CommandLogs commandToChange=null;
                    for(CommandLogs c : CommandLogs.listOfCommands)
                    {
                      if(c.commandId == cmd.commandId)
                      {
                        index = CommandLogs.listOfCommands.indexOf(c);
                        commandToChange=c;
                        break;
                      }
                    }
                    commandToChange.status ="D";
                    CommandLogs.listOfCommands.set(index, commandToChange);
                    System.out.println("LIST AFTER IS: "+CommandLogs.listOfCommands);

                    for(CommandLogs c2 : CommandLogs.listOfCommands)
                    {
                      System.out.println(c2.commandId+" "+c2.status+" "+c2.fileName);
                    }
                }

                // close input and output streams
                if (inputString.equalsIgnoreCase("quit"))
                {
                    input.close();
                    output.close();
                    socket.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
