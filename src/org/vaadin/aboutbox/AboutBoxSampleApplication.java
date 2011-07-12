package org.vaadin.aboutbox;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class AboutBoxSampleApplication extends Application {

    private static final long serialVersionUID = 1346589914301518898L;

    @Override
	public void init() {
		Window mainWindow = new Window("AboutBox Sample Application");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
