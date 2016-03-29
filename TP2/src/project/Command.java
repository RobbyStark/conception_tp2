package project;

import java.util.Vector;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import commands.ICommand;

/**
* Class that encapsulates everything directly related to commands (plugins) :
* the ICommand object, the Text and Button objects and the method to run the
* command.
* @author  Francis De LaSalle et Matthieu Boglioni
* @version 1.0
* @since   2016-03-03
*******************************************************************************/
public class Command {
	/**
	 * A reference to the ICommand object (the plugin).
	 */
	private ICommand command;
	
	/**
	 * A reference to the text field used by the command.
	 */
	private Text commandText;
	
	
	/**
	 * A reference to the button used by the command.
	 */
	private Button commandButton;
	
	/**
	 * The constructor of the Command class, assigns the associated plugin.
	 */
	public Command(ICommand loadedCommand) {
		command = loadedCommand;
	}
	
	/**
	 * Method that returns the command (the plugin).
	 */
	public ICommand getCommand() {
		return command;
	}
	
	/**
	 * Setter for the Text object representing the associated text field.
	 */
	public void setCommandText(Text text) {
		commandText = text;
	}
	
	/**
	 * Getter for the Text object representing the associated text field.
	 */
	public Text getCommandText() {
		return commandText;
	}
	
	/**
	 * Setter for the Button object representing the associated button.
	 */
	public void setCommandButton(Button button) {
		commandButton = button;
	}
	
	/**
	 * Getter for the Button object representing the associated button.
	 */
	public Button getCommandButton() {
		return commandButton;
	}
	
	/**
	 * Method that calls the run method of the command (plugin) if it supports
	 * the type of file currently selected and updates the text field.
	 */
	public void run() {
		// Run the command if allowed to (correct File type).
		if ((CommandsPart.currentFile.isFile() && command.getSupportFile()) ||
				(CommandsPart.currentFile.isDirectory() && command.getSupportFolder())) {
			String result = command.run(CommandsPart.currentFile);
			
			// Updates the Text field.
			commandText.setText(result);
			TreeViewerPart.refresh();
		}
	}
}
