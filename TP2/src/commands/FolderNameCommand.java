
package commands;

import java.io.File;

/**
 * This commands returns the folder name of a node.
 */

public class FolderNameCommand implements ICommand {

	@Override
	public String run(File file) {
		return file.getName();
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