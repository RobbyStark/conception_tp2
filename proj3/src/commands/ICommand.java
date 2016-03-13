

package commands;

import java.io.File;

/**
 * Abstract interface for all commands.
 */
public abstract interface ICommand {
	
	/**
	 * Method to run the command.
	 * @param file the file on which to run the command.
	 * @return the absolute path of the file.
	 */
	public String run(File file);
	
	/**
	 * This method returns whether or not this command can be invoked of folders.
	 * @return true if the command supports folders
	 */
	public boolean getSupportFolder();
	
	/**
	 * This method returns whether or not this command can be invoked on files.
	 * @return true if the command supports files
	 */
	public boolean getSupportFile();
}