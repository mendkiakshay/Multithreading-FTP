import java.util.*;
public class CommandLogs
{
  public  int commandId;
  public  String status;  //E for executing, D for Done, T for Terminated
  public  String fileName;
  public static List<CommandLogs> listOfCommands = new ArrayList<CommandLogs>();
  public static int cmdIDCounter = 0;

  CommandLogs(String stat, String file)
  {
    this.commandId = cmdIDCounter++;
    this.status = stat;
    this.fileName = file;
    listOfCommands.add(this);
  }
}
