import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class myFtpServerProcess {

	String serverPath=System.getProperty("user.dir");


	// splitCommand splits the string with space
	public static String[] splitCommand(String command) {
		return command.split(" ");
	}
	public boolean get(DataOutputStream output,String inputString)
	{
    try
    {
        // take file requested by Client into FileInputStream
        FileInputStream myFile = new FileInputStream(splitCommand(inputString)[1]);
        int characters;
        do {
            // read the characters and write them into files
            characters = myFile.read();
            output.writeUTF(String.valueOf(characters));
        } while (characters != -1);
        myFile.close();
        return true;
    }
    catch (IOException ex)
		{
					System.out.println(ex.getMessage());
			    return false;
    }

	}
	public boolean put(DataInputStream input, String inputString)
	{
            try
						{
								FileOutputStream fileoutput = null;
                String filePath = "";
                String fileName = "";
                // extract file name from file path
                if (splitCommand(inputString)[1].contains("/"))
                {
                    filePath = splitCommand(inputString)[1];
                    String[] pathArray = filePath.split("/");
                    fileName = pathArray[pathArray.length - 1];
                }
                else
                {
                    fileName = splitCommand(inputString)[1];
                }   // get current path of Server
                File file = new File(serverPath+"/"+fileName);
                // String s = file.getAbsolutePath();
                // create blank file with same name at current path of
                // Server
                fileoutput = new FileOutputStream(file.getAbsolutePath());
								//System.out.println("Server path will be: "+file.getAbsolutePath());

                int characters;
                // read characters coming from inputStream from Client
                do {
                    characters = Integer.parseInt(input.readUTF());
                    if (characters != -1)
                    {
                        // Write characters to blank file
                        fileoutput.write(characters);
                    }
                } while (characters != -1);
                fileoutput.close();
                System.out.println("File is Received");
                return true;
            }
            catch (Exception ex)
						{
							System.out.println(ex.getMessage());
							            return false;
            }
	}
	//Method to processing mkdir command given by the user
	public String mkdir(String folderName) {
		try {
			// File file1 = new File(System.getProperty("user.dir"));
			File file1 = new File(serverPath);
			File directory = new File(file1.getAbsolutePath() + "/" + folderName);
			directory.mkdir();
			return "success";
		} catch (Exception ex) {
			return ex.toString();
		}
	}

	//Method that sets current directory with the new Folder
	public String setCurrent(String folderName)
	{
		try {
			// creates new directory for new folder
			if(folderName.contains("/"))
			{
				File directory = new File(folderName);
				serverPath= directory.getAbsolutePath();
				//System.setProperty("user.dir", directory.getAbsolutePath()); // Sets
				//Set this directory as current directory
				File file = new File(serverPath);
				return file.getAbsolutePath(); // returns Path of new directory
			}
			else
			{
				File file = new File(serverPath);
				String s  = file.getAbsolutePath();
				File f = new File(s+"/"+folderName);
				serverPath=f.getAbsolutePath();
				return serverPath;

			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.toString());
			return "error";
		}
	}

	//Method that sets current directory to its previous directory
	public String setPrevious() {
		try {
			// File file = new File(System.getProperty("user.dir"));
			File file = new File(serverPath);
			// Gets parent folder's path
			String parentPath = file.getAbsoluteFile().getParentFile().getAbsolutePath();
			serverPath= parentPath;

			// Sets current director as parent's
			// System.setProperty("user.dir", parentPath);

			// Gets current path
			//file = new File(System.getProperty("user.dir"));
			//return file.getAbsolutePath();
			return serverPath;
		} catch (Exception ex) {
			return ex.toString();
		}
	}

	// Method to delete a file
	public String delete(String fileName) {
		if (fileName.contains("/")) {
			File deleteFile = new File(fileName);
			if (deleteFile.delete()) {
				return "File is deleted";
			} else
				return "Problem deleting File";
		}
		else{
			// File file = new File(System.getProperty("user.dir"));
			File file = new File(serverPath);
			String s  = file.getAbsolutePath();
			File f = new File(s+"/"+fileName);
			//Delete the file if it exists
			if (f.delete()) {
				return "File is deleted";
			} else
				return "Problem deleting File";
		}
	}

	// Method to list files and subdir- ls command with dir path
	public File[] ls(File dir) {
		File[] files = dir.listFiles();
		return files;
	}

	//Method to list files and subdir- ls command on current dir
	public File[] ls() {
		//Getting user's current directory
		// File dir = new File(System.getProperty("user.dir"));
		File dir = new File(serverPath);
		//Getting files and directories from current directory and storing it in a array
		File[] files = dir.listFiles();
		return files;
	}

	// Method to print current dir- pwd
	public String pwd(File file) {
		// file = new File(System.getProperty("user.dir"));
		file = new File(serverPath);
		return "Current Working Directory: " + file.getAbsolutePath();
	}

}
