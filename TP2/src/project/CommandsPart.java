
package project;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import commands.*;
/**
* This class implements UI section that displays the different commands and controls 
* @author  Francis De LaSalle et Matthieu Boglioni
* @version 1.0
* @since   2016-03-03
*
* code inspiré de :
* Copyright (c) 2010 - 2013 IBM Corporation and others.
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
public class CommandsPart {
	
	private static File currentFile;
	private static Button autorunButton;
	/**
	* A vector containing a reference to the ICommand objects (the plugins).
	*/
	private static Vector<ICommand> commands = new Vector<ICommand>();
	
	/**
	 A vector containing a reference to all text fields used by the commands.
	*/
	private static Vector<Text> commandsText = new Vector<Text>();
	
	
	/**
	 A vector containing a reference to all buttons used by the commands.
	*/
	private static Vector<Button> commandsButton = new Vector<Button>();
	
	/**
	 A map for the button names and the index of the corresponding element in the
	 other 3 vectors.
	 */
	private static Map<String, Integer> commandNameIndexMap = new HashMap<String, Integer>();

	@PostConstruct
	/**
     * creates the UI section that displays the different commands and controls 
	 * @param parent the parent composite.
	 */	
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));	
		
		// Loads the commands (plugins) from the extension point.
		CommandsLoader.load();
		
		// Create the Button and Text elements.
		for (ICommand command : commands) {
			Button button = new Button(parent, SWT.NONE);
			String commandName = command.getClass().getName().replaceFirst("commands.", "");
			button.setText(commandName);
			button.setData(commandName);
			button.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					switch (e.type) {
					case SWT.Selection:
						CommandsPart.executeCommand(commandName);
						break;
					}
				}
			});
			button.setEnabled(false);
			commandsButton.addElement(button);
			
			Text text = new Text(parent, SWT.READ_ONLY | SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			text.setEnabled(false);
			commandsText.addElement(text);
		}

		// Creates the autorun Button.
		autorunButton = new Button(parent, SWT.CHECK);
		autorunButton.setText("Autorun");

		// Creates the clear Button.
		Button clearButton = new Button(parent, SWT.NONE);
		clearButton.setText("Clear");
		clearButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					clear();
					break;
				}
			}
		});
	}

	/**
     * clears the output fields for the commands results
	 * @param none
	 */	
	public static void clear() {
		
		for (Text text : commandsText) {
			text.setText("");
		}
	}

	public static boolean isAutorun() {
		return autorunButton.getSelection();		
	}
	
	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
	}
	
	
	/**
     * Method called when an item of the tree is selected. Updates the currentFile
	 * and the buttons availability. Parameter is the file item absolute path.
	 * @param none
	 */	
	public static void selectedItem() {
		currentFile = (File) TreeViewerPart.getCurrentFile();
		
		int index = 0;
		for (ICommand command : commands) {
			// Enable the Button and Text field if supported.
			if ((currentFile.isFile() && command.getSupportFile()) ||
					(currentFile.isDirectory() && command.getSupportFolder())) {
				commandsText.get(index).setEnabled(true);
				commandsButton.get(index).setEnabled(true);
				
				// If autorun was enabled, execute the command.
				if (isAutorun()) {
					executeCommand((String) commandsButton.get(index).getData());
				}
			} else {
				// Disable otherwise.
				commandsText.get(index).setEnabled(false);
				commandsButton.get(index).setEnabled(false);
			}
			index++;
		}
	}
	
	/**
	 * addss a command (plugin) to the commands vector.
	 * @param command the new command
	 */	
	public static void addCommand(ICommand command) {
		commands.addElement(command);
		commandNameIndexMap.put(
				command.getClass().getName().replaceFirst("commands.", ""),
				commands.size() - 1);
	}
	
	/**
	 * executes a command
	 * @param commandName the name of the command, must be the exact name of the corresponding class
	 */	
	public static void executeCommand(String commandName) {
		// Obtain the command from the vector.
		int commandIndex = commandNameIndexMap.get(commandName);
		ICommand command = commands.get(commandIndex);
		
		// Run the command if allowed to (correct File type).
		if ((currentFile.isFile() && command.getSupportFile()) ||
				(currentFile.isDirectory() && command.getSupportFolder())) {
			String result = command.run(currentFile);
			
			// Updates the Text field.
			commandsText.get(commandIndex).setText(result);
			TreeViewerPart.refresh();
		}
	}
}