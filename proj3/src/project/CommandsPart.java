/*******************************************************************************
 * code inspiré de:
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package project;

import java.io.File;
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

public class CommandsPart {

	private static Text absolutePathText;
	private static Text fileNameText;
	private static Text folderNameText;
	private static Button absolutePathButton;
	private static Button fileNameButton;
	private static Button folderNameButton;
	private static File currentFile;
	private static Button autorunButton;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));		
		
	    absolutePathButton = new Button(parent, SWT.NONE);
	    absolutePathButton.setText("absolutePathCommand");
	    absolutePathButton.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event e) {
	        switch (e.type) {
	        case SWT.Selection:
	          runAbsolutePathCommand();
	          break;
	        }
	      }
	    });		
	    absolutePathButton.setEnabled(false);
	
		absolutePathText = new Text(parent, SWT.READ_ONLY | SWT.BORDER);
		absolutePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
	    fileNameButton = new Button(parent, SWT.NONE);
	    fileNameButton.setText("fileNameCommand");
	    fileNameButton.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event e) {
	        switch (e.type) {
	        case SWT.Selection:
	          runFileNameCommand();
	          break;
	        }
	      }
	    });		
	    fileNameButton.setEnabled(false);
	
		fileNameText = new Text(parent, SWT.READ_ONLY | SWT.BORDER);
		fileNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
	    folderNameButton = new Button(parent, SWT.NONE);
	    folderNameButton.setText("folderNameCommand");
	    folderNameButton.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event e) {
	        switch (e.type) {
	        case SWT.Selection:
	          runFolderNameCommand();
	          break;
	        }
	      }
	    });
	    folderNameButton.setEnabled(false);
	
		folderNameText = new Text(parent, SWT.READ_ONLY | SWT.BORDER);
		folderNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		autorunButton = new Button(parent, SWT.CHECK);
		autorunButton.setText("Autorun");
		 
		Button clearButton = new Button(parent, SWT.NONE);
		clearButton.setText("Clear");
		clearButton.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		        switch (e.type) {
		        case SWT.Selection:
		          currentFile = null;
		          resetFields(null);
		          break;
		        }
		      }
		    });
		
	}
	
	public static void runAbsolutePathCommand(){						
		File file = (File) TreeViewerPart.getCurrentFile();	
		resetFields(file);
		ICommand command = new AbsolutePathCommand();		
		absolutePathText.setText(command.run(file));		
	}
	
	public static void runFileNameCommand(){						
		File file = (File) TreeViewerPart.getCurrentFile();	
		resetFields(file);
		ICommand command = new FileNameCommand();		
		fileNameText.setText(command.run(file));
	}
	
	public static void runFolderNameCommand(){						
		File file = (File) TreeViewerPart.getCurrentFile();
		resetFields(file);
		ICommand command = new FolderNameCommand();		
		folderNameText.setText(command.run(file));
	}
	public static void AbsolutePathCommandSetEnabled(boolean isEnabled){
		 absolutePathButton.setEnabled(isEnabled);
	}
	
	public static void FileNameCommandSetEnabled(boolean isEnabled){
		 fileNameButton.setEnabled(isEnabled);
	}
	
	public static void FolderNameCommandSetEnabled(boolean isEnabled){
		folderNameButton.setEnabled(isEnabled);
	}
	
	public static void resetFields(File file){
		
		if (currentFile == null) {		
			absolutePathText.setText("");
			fileNameText.setText("");
			folderNameText.setText("");
		}
		else if(!currentFile.equals(file)){			
			absolutePathText.setText("");
			fileNameText.setText("");
			folderNameText.setText("");
		}
		currentFile = file;
	}
	
	public static void autoRun(){
		if (absolutePathButton.isEnabled()){
			runAbsolutePathCommand();
		}	
		if (fileNameButton.isEnabled()){
			runFileNameCommand();
		}
		if (folderNameButton.isEnabled()){
			runFolderNameCommand();
		}
	}
	
	public static boolean isAutorun(){
		return autorunButton.getSelection();		
	}
	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
	}
}