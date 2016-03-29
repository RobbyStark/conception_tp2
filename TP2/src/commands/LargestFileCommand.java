
package commands;

import java.io.File;

/**
 * This commands returns the largest file's name inside the given folder
 */
public class LargestFileCommand implements ICommand {
	
	@Override
	public String run(File file) {		
		
		File[] listOfFiles = file.listFiles();
		File largestFile = null;
		for (File child : listOfFiles) {
		    if (child.isFile()) {
		    	if (largestFile == null ||child.length()>largestFile.length()){
		    		largestFile = child;
		    	}		    
		    }
		}
		
		if (largestFile != null)
			return largestFile.getName();
		else 
			return "";
	}

	@Override
	public boolean getSupportFolder() {
		return true;
	}

	@Override
	public boolean getSupportFile() {
		return false;
	}
}