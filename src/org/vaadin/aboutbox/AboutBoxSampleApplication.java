package org.vaadin.aboutbox;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class AboutBoxSampleApplication extends Application {

    private static final long serialVersionUID = 1346589914301518898L;

    @Override
    public void init() {
        Window w = new Window("AboutBox Sample");
        w.addComponent(new Label("Hello World!"));
        w.addComponent(new AboutBox("AboutBox", "Hello AboutBox!"));
        setMainWindow(w);


    }

}
