
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
	
	public static File currentFile;
	private static Button autorunButton;
	
	private static Vector<Command> commands = new Vector<Command>();

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
		for (Command command : commands) {
			Button button = new Button(parent, SWT.NONE);
			String commandName = command.getCommand().getClass().getName().replaceFirst("commands.", "");
			button.setText(commandName);
			button.setData(commandName);
			button.setData(command);
			button.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					switch (e.type) {
					case SWT.Selection:
						Command command = (Command) e.widget.getData();
						command.run();
						break;
					}
				}
			});
			button.setEnabled(false);
			command.setCommandButton(button);
			
			Text text = new Text(parent, SWT.READ_ONLY | SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			text.setEnabled(false);
			command.setCommandText(text);
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
		for (Command command : commands) {
			command.getCommandText().setText("");
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
		for (Command command : commands) {
			// Enable the Button and Text field if supported.
			if ((currentFile.isFile() && command.getCommand().getSupportFile()) ||
					(currentFile.isDirectory() && command.getCommand().getSupportFolder())) {
				command.getCommandText().setEnabled(true);
				command.getCommandButton().setEnabled(true);
				
				// If autorun was enabled, execute the command.
				if (isAutorun()) {
					command.run();
				}
			} else {
				// Disable otherwise.
				command.getCommandText().setEnabled(false);
				command.getCommandButton().setEnabled(false);
			}
		}
	}
	
	/**
	 * Adds a new Command object to the commands vector.
	 * @param command the new command
	 */	
	public static void addCommand(Command command) {
		commands.addElement(command);
	}
}