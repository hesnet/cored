package org.vaadin.cored;

import java.io.File;
import java.io.IOException;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.MenuBar;

@SuppressWarnings("serial")
public class CoredMenuBar extends MenuBar {
	
	private MenuItem projectItem;
	
	private MenuItem userItem;
	
	private final IDE ide;
	private final Project project;
	
	public CoredMenuBar() {
		this(null);
	}
	
	public CoredMenuBar(IDE ide) {
		super();
		this.ide = ide;
		this.project = ide!=null ? ide.getProject() : null;
		setWidth("100%");
		
		if (project!=null){
			projectItem = addItem("Project", null);
			projectItem.setIcon(Icons.BOX);
			projectItem.addItem("Download as zip", new DownloadCommand()).setIcon(Icons.BOX_ZIPPER);
			projectItem.addItem("Project timeline", new StatsCommand()).setIcon(Icons.APPLICATION_WAVE);
			projectItem.addItem("Leave project", new CloseCommand());
		
			project.addMenuItem(this);
			
		}
		
		
		
		userItem = addItem("User", null);
		userItem.setIcon(Icons.USER_SILHOUETTE);
		userItem.addItem("Log out", new LogoutCommand());
		
	}
	
	private class DownloadCommand implements Command {
		public void menuSelected(MenuItem selectedItem) {
			File temp;
			try {
				temp = File.createTempFile("cored-"+project.getName(), ".zip");
				System.out.println("Zipping to "+temp);
				project.zip(temp);
				FileResource zip = new FileResource(temp, getApplication());
				getWindow().open(zip);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private class CloseCommand implements Command {
		public void menuSelected(MenuItem selectedItem) {
			ide.leaveIDE();
		}
	}
	
	private class LogoutCommand implements Command {
		public void menuSelected(MenuItem selectedItem) {
			CoredApplication.getInstance().setCoredUser(null);
			ide.leaveIDE();
		}
	}
	
	private class StatsCommand implements Command {
		public void menuSelected(MenuItem selectedItem) {
			StatsWindow win = new StatsWindow(ide.getProject());
			getWindow().addWindow(win);
		}
	}
}
