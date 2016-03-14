package project;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
/**
* This class implements the file chooser for root selection 
* @author  Francis De LaSalle et Matthieu Boglioni
* @version 1.0
* @since   2016-03-03
*
* Code inspiré de:
* Add A File Chooser/Selector for Eclipse RCP Development
* http://www.programcreek.com/2010/11/add-a-file-chooserselector-for-eclipse-rcp-development/
*******************************************************************************/
public class FileChooser extends Composite {

	Text mText;
	Button mButton;
	String title = null;

	/**
	 * constructor for the file choose
	 * @param parent the parent composite
	 */	
	public FileChooser(Composite parent) {
		super(parent, SWT.NULL);
		createContent();
	}

	/**
	 * sets the UI of the file chooser
	 */	
	public void createContent() {
		GridLayout layout = new GridLayout(2, false);
		setLayout(layout);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;


		mButton = new Button(this, SWT.NONE);
		mButton.setText("Choose root");
		mButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(mButton.getShell(),  SWT.OPEN  );
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null) return;
				File myFile = new File (path);				
				TreeViewerPart.setRoot(myFile);
			}
		});
	}


	/**
	 * gets the selected file
	 * @return the selected file
	 */	
	public File getFile() {
		String text = mText.getText();
		if (text.length() == 0) return null;
		return new File(text);
	}

	/**
	 * gets the title
	 * @return the title
	 */	
	public String getTitle() {
		return title;
	}

	/**
	 * sets the title
	 * @param title the new title
	 */	
	public void setTitle(String title) {
		this.title = title;
	}
}