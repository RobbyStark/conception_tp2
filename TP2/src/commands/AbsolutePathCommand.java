


package commands;

import java.io.File;

/**
 * This commands returns the absolute path of a file or directory.
 */
public class AbsolutePathCommand implements ICommand {

	
	@Override
	public String run(File file) {
		return file.getAbsolutePath();
	}

	@Override
	public boolean getSupportFolder() {
		return true;
	}

	@Override
	public boolean getSupportFile() {
		return true;
	}
}