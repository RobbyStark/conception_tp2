
/*******************************************************************************
 * Code inspir� de:
 * Eclipse JFace Tree - Tutorial
 * Section 3
 * http://www.vogella.com/tutorials/EclipseJFaceTree/article.html
 *******************************************************************************/

package project;

import java.io.File;
import java.net.URL;
import javax.annotation.PostConstruct;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class TreeViewerPart {
  private static TreeViewer viewer;

  @PostConstruct
  public void createControls(Composite parent) {	  
	  
    viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL |SWT.NONE);
    viewer.setContentProvider(new ViewContentProvider());
    viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ViewLabelProvider(createImageDescriptor())));
    
    
    viewer.addDoubleClickListener(new IDoubleClickListener() {
    	  @Override
    	  public void doubleClick(DoubleClickEvent event) {
    	    TreeViewer viewer = (TreeViewer) event.getViewer();
    	    IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection(); 
    	    Object selectedNode = thisSelection.getFirstElement(); 
    	    viewer.setExpandedState(selectedNode,
    	        !viewer.getExpandedState(selectedNode));
    	  }
    	}); 

    
    Tree tree = (Tree) viewer.getControl();
    tree.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        TreeItem item = (TreeItem) e.item;
        CommandsPart.AbsolutePathCommandSetEnabled(true);
        item.setExpanded(!item.getExpanded());
          if (item.getItemCount() == 0) {           
            CommandsPart.FileNameCommandSetEnabled(true); 
            CommandsPart.FolderNameCommandSetEnabled(false);
          }
          else {
        	  CommandsPart.FileNameCommandSetEnabled(false); 
        	  CommandsPart.FolderNameCommandSetEnabled(true); 
          }
          if (CommandsPart.isAutorun()){
        	  CommandsPart.autoRun();
          }
          viewer.refresh();
        }
    }); 
        
	new FileChooser(parent);        
    
  }

  private ImageDescriptor createImageDescriptor() {
    Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
    URL url = FileLocator.find(bundle, new Path("icons/folder.png"), null);
    return ImageDescriptor.createFromURL(url);
  }

  class ViewContentProvider implements ITreeContentProvider {
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public Object[] getElements(Object inputElement) {
      return (File[]) inputElement;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
      File file = (File) parentElement;
      return file.listFiles();
    }

    @Override
    public Object getParent(Object element) {
      File file = (File) element;
      return file.getParentFile();
    }
    

    @Override
    public boolean hasChildren(Object element) {
      File file = (File) element;
      if (file.isDirectory()) {
        return true;
      }
      return false;
    }
  }

  class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {
    
    private ImageDescriptor directoryImage;
    private ResourceManager resourceManager;

    public ViewLabelProvider(ImageDescriptor directoryImage) {
      this.directoryImage = directoryImage;
    }

    @Override
    public StyledString getStyledText(Object element) {
      if(element instanceof File) {
        File file = (File) element;
        StyledString styledString = new StyledString(getFileName(file));
        String[] files = file.list();
        if (files != null) {
          styledString.append(" (" + files.length + ") ",
              StyledString.COUNTER_STYLER);
        }
        return styledString;
      }
      return null;
    }
    
    @Override
    public Image getImage(Object element) {
      if(element instanceof File) {
        if(((File) element).isDirectory()) {
          return getResourceManager().createImage(directoryImage);
        }
      }
      
      return super.getImage(element);
    }
    
    @Override
    public void dispose() {
      if(resourceManager != null) {
        resourceManager.dispose();
        resourceManager = null;
      }
    }
    
    protected ResourceManager getResourceManager() {
      if(resourceManager == null) {
        resourceManager = new LocalResourceManager(JFaceResources.getResources());
      }
      return resourceManager;
    }

    private String getFileName(File file) {
      String name = file.getName();
      return name.isEmpty() ? file.getPath() : name;
    }
  }
  
	public static Object getCurrentFile(){		
		return  ((TreeSelection) viewer.getSelection()).getFirstElement();
	}
	
	public static void setRoot(File file){	
		 viewer.setInput(file.listFiles());
		 CommandsPart.resetFields(null);
		 CommandsPart.FileNameCommandSetEnabled(false); 
		 CommandsPart.FolderNameCommandSetEnabled(false); 
		 CommandsPart.AbsolutePathCommandSetEnabled(false); 
	}
  
  @Focus
  public void setFocus() {
  }
} 