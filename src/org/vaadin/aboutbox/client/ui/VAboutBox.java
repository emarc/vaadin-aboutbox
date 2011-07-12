package org.vaadin.aboutbox.client.ui;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VAboutBox extends Widget implements Paintable, ClickHandler {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-aboutbox";

    public static final String CLICK_EVENT_IDENTIFIER = "lc";

    private static final String CLASS_TITLE = "t";

    private static final String CLASS_DESCRIPTION = "d";

    private static final String CLASS_LINK = "l";

    private static final String CLASS_CAPTION = "c";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    private DivElement aboutDiv;

    private DivElement captionDiv;

    private DivElement titleDiv;

    private DivElement descriptionDiv;

    private DivElement linkDiv;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VAboutBox() {
        aboutDiv = Document.get().createDivElement();
        setElement(aboutDiv);
        setStyleName(CLASSNAME);

        // The main title when expanded
        titleDiv = Document.get().createDivElement();
        titleDiv.setClassName(CLASS_TITLE);
        aboutDiv.appendChild(titleDiv);

        // Description text (html)
        descriptionDiv = Document.get().createDivElement();
        descriptionDiv.setClassName(CLASS_DESCRIPTION);
        aboutDiv.appendChild(descriptionDiv);

        // Link to more information
        linkDiv = Document.get().createDivElement();
        linkDiv.setClassName(CLASS_LINK);
        aboutDiv.appendChild(linkDiv);

        // Small caption when collapsed
        captionDiv = Document.get().createDivElement();
        captionDiv.setClassName(CLASS_CAPTION);
        aboutDiv.appendChild(captionDiv);

        // TODO: Click handler
    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        String caption = uidl.getStringAttribute("caption");
        String title = uidl.getStringAttribute("title");
        String description = uidl.getStringAttribute("description");
        String linkCaption = uidl.getStringAttribute("linkCaption");
        boolean open = uidl.getBooleanAttribute("open");

        captionDiv.setInnerText(caption);
        titleDiv.setInnerHTML(title);
        descriptionDiv.setInnerHTML(description);
        linkDiv.setInnerText(linkCaption);

        // Open or close
        if (open) {
            open();
        } else {
            close();
        }

    }

    /**
     * Called when a native click event is fired.
     *
     * @param event
     *            the {@link ClickEvent} that was fired
     */
    public void onClick(ClickEvent event) {
        client.updateVariable(paintableId, CLICK_EVENT_IDENTIFIER, true, true);
    }

    /**
     * Highlights the AboutBox
     *
     */
    public void highlight() {
        aboutDiv.addClassName("hl");
    }

    /**
     * Un-highlights the AboutBox
     *
     */
    public void unHighlight() {
        aboutDiv.removeClassName("hl");
    }

    /**
     * Open the AboutBox
     *
     */
    public void open() {
        aboutDiv.addClassName("open");
    }

    /**
     * Close the AboutBox
     *
     */
    public void close() {
        aboutDiv.removeClassName("open");
    }

}
