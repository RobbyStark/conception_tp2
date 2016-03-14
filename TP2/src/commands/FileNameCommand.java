
package commands;

import java.io.File;

/**
 * This commands returns the file name of a node.
 */
public class FileNameCommand implements ICommand {
	
	@Override
	public String run(File file) {
		return file.getName();
	}

	@Override
	public boolean getSupportFolder() {
		return false;
	}

	@Override
	public boolean getSupportFile() {
		return true;
	}
}