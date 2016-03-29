package project;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;

import commands.ICommand;

/**
* Class that provides a method to load the commands (plugins) from the
* extension point.
* @author  Francis De LaSalle et Matthieu Boglioni
* @version 1.0
* @since   2016-03-03
*******************************************************************************/
public class CommandsLoader {
	/**
	* the command extension id, in string form
	*/
	private static final String COMMANDEXTENSION_ID = 
			"com.proj3.commandExtension";
	@Execute
	/**
	 * Method that loads plugins from the extension registry and create the
	 * associated Command object.
	 */	
	public static void load() {
		IConfigurationElement[] config =
				Platform.getExtensionRegistry().getConfigurationElementsFor(COMMANDEXTENSION_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object o =
						e.createExecutableExtension("class");
				if (o instanceof ICommand) {
					// Adds this command (plugin) to the vector of commands.	
					CommandsPart.addCommand(new Command((ICommand) o));
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}
}