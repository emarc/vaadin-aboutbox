package org.vaadin.aboutbox.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.AlignmentInfo;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VAboutBox extends Widget implements Paintable {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-aboutbox";

    private static final String CLASS_ABOUTBOX = "about";

    public static final String CLICK_EVENT_IDENTIFIER = "lc";

    private static final String CLASS_TITLE = "t";

    private static final String CLASS_DESCRIPTION = "d";

    private static final String CLASS_LINK = "l";

    private static final String CLASS_CAPTION = "c";

    private static final String CLASS_SHADOW = "s";

    public static final String STYLE_DEFAULT = "leather";

    public static final String STYLE_NONE = "none";

    private static final String CSS_TEME_ID = "aboutbox-theme";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    private DivElement aboutDiv;

    private DivElement captionDiv;

    private DivElement titleDiv;

    private DivElement descriptionDiv;

    private DivElement linkDiv;

    private String boxStyle = STYLE_NONE;

    private DivElement shadowDiv;

    private AlignmentInfo alignment;

    private int offset;

    private DivElement rootDiv;

    private boolean disableLink;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VAboutBox() {
        rootDiv = Document.get().createDivElement();
        setElement(rootDiv);
        setStyleName(CLASSNAME);

        // The main title when expanded
        aboutDiv = Document.get().createDivElement();
        aboutDiv.setClassName(CLASS_ABOUTBOX);
        rootDiv.appendChild(aboutDiv);

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

        // Shadow
        shadowDiv = Document.get().createDivElement();
        shadowDiv.setClassName(CLASS_SHADOW);
        rootDiv.appendChild(shadowDiv);

        // Click handler
        addDomHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                if (!disableLink &&  event.getNativeEvent().getEventTarget() == (Object) linkDiv) {
                    linkClicked();
                } else {
                    toggleOpen();
                }
            }
        }, ClickEvent.getType());

        addDomHandler(new MouseOverHandler() {

            public void onMouseOver(MouseOverEvent event) {
                highlight();
            }

        }, MouseOverEvent.getType());

        addDomHandler(new MouseOutHandler() {

            public void onMouseOut(MouseOutEvent event) {
                unHighlight();
            }
        }, MouseOutEvent.getType());

    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, false)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        String style = uidl.getStringAttribute("boxStyle");
        if (!boxStyle.equals(style)) {
            setBoxStyle(style);
        }
        String caption = uidl.getStringAttribute("caption");
        captionDiv.setInnerText(caption);

        String title = uidl.getStringAttribute("title");
        titleDiv.setInnerHTML(title);

        String description = uidl.getStringAttribute("description");
        descriptionDiv.setInnerHTML(description);

        if (uidl.hasAttribute("linkCaption")) {
            String linkCaption = uidl.getStringAttribute("linkCaption");
            linkDiv.setInnerText(linkCaption);
            disableLink = false;
            linkDiv.getStyle().setCursor(Cursor.POINTER);
        } else {
            disableLink = true;
            linkDiv.getStyle().setCursor(Cursor.AUTO);
        }

        alignment = new AlignmentInfo(uidl.getIntAttribute("align"));
        offset = uidl.getIntAttribute("offset");

        // Apply box position
        applyPosition();

        // Open or close
        if (uidl.hasAttribute("open")) {
            if (uidl.getBooleanAttribute("open")) {
                open();
            } else {
                close();
            }
        }

    }

    private void applyPosition() {

        // Defaults
        if (alignment == null) {
            alignment = new AlignmentInfo(AlignmentInfo.RIGHT,
                    AlignmentInfo.TOP);
        }
        if (offset < 0) {
            offset = 100;
        }

        if (alignment.isRight()) {
            aboutDiv.getStyle().setRight(offset - 10, Unit.PX);
            aboutDiv.getStyle().clearLeft();
            shadowDiv.getStyle().setRight(offset, Unit.PX);
            shadowDiv.getStyle().clearLeft();
        } else {
            aboutDiv.getStyle().setLeft(offset-10, Unit.PX);
            aboutDiv.getStyle().clearRight();
            shadowDiv.getStyle().setLeft(offset, Unit.PX);
            shadowDiv.getStyle().clearRight();
        }

    }

    private void setBoxStyle(String boxStyle) {
        this.boxStyle = boxStyle;
        loadCss(GWT.getModuleBaseURL() + "aboutbox/" + boxStyle + ".css",
                "all", CSS_TEME_ID);
    }

    private native void loadCss(String url, String media, String id) /*-{

                                                                     var links = $doc.getElementsByTagName("link");
                                                                     var es = null;
                                                                     if (links.length > 0) {
                                                                     for (var i = 0; i<links.length; i++) {
                                                                     if (links[i]["id"] == id) {
                                                                     if (links[i]["href"] == url) {
                                                                      return;
                                                                     } else {
                                                                        es = links[i];
                                                                        break;
                                                                     }
                                                                     }
                                                                     }
                                                                     }

                                                                     if (es == null) {
                                                                     es = $doc.createElement("link");
                                                                     }
                                                                     es.setAttribute("id", id);
                                                                     es.setAttribute("rel", "stylesheet");
                                                                     es.setAttribute("type", "text/css");
                                                                     es.setAttribute("media", media);
                                                                     es.setAttribute("href", url);

                                                                     var head = $doc.getElementsByTagName("head")[0];
                                                                     head.appendChild(es);
                                                                     }-*/;

    /**
     * Called when a native click event is fired.
     *
     */
    public void linkClicked() {
        client.updateVariable(paintableId, CLICK_EVENT_IDENTIFIER, true, true);
    }

    /**
     * Highlights the AboutBox when it is not open.
     *
     */
    public void highlight() {
        if (!isOpen()) {
            aboutDiv.addClassName("hl");
        }
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
        unHighlight();
        aboutDiv.addClassName("open");
    }

    /**
     * Open or close the about box depending on its current state.
     */
    protected void toggleOpen() {
        if (isOpen()) {
            close();
        } else {
            open();
        }
    }

    /**
     * Is the about box currently open.
     *
     * @return
     */
    public boolean isOpen() {
        return aboutDiv.getClassName().indexOf("open") >= 0;
    }

    /**
     * Close the AboutBox
     *
     */
    public void close() {
        aboutDiv.removeClassName("open");
    }

}
