import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class myFtpServerProcess {
	
	//Method to processing mkdir command given by the user
	public String mkdir(String folderName) {
		try {
			File file1 = new File(System.getProperty("user.dir"));
			File directory = new File(file1.getAbsolutePath() + "/" + folderName);
			directory.mkdir();
			return "success";
		} catch (Exception ex) {
			return ex.toString();
		}
	}

	//Method that sets current directory with the new Folder
	public String setCurrent(String folderName) {
		try {
			// creates new directory for new folder
			File directory = new File(folderName);
			System.setProperty("user.dir", directory.getAbsolutePath()); // Sets
			//Set this directory as current directory
			File file = new File(System.getProperty("user.dir"));
			return file.getAbsolutePath(); // returns Path of new directory
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return "error";
		}
	}

	//Method that sets current directory to its previous directory
	public String setPrevious() {
		try {
			File file = new File(System.getProperty("user.dir"));
			// Gets parent folder's path
			String parentPath = file.getAbsoluteFile().getParentFile().getAbsolutePath();
			// Sets current director as parent's
			System.setProperty("user.dir", parentPath);
			// Gets current path
			file = new File(System.getProperty("user.dir"));
			return file.getAbsolutePath();
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
			File file = new File(System.getProperty("user.dir"));
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
		File dir = new File(System.getProperty("user.dir"));
		//Getting files and directories from current directory and storing it in a array
		File[] files = dir.listFiles();
		return files;
	}

	// Method to print current dir- pwd
	public String pwd(File file) {
		file = new File(System.getProperty("user.dir"));
		return "Current Working Directory: " + file.getAbsolutePath();
	}

}
