package org.vaadin.aboutbox;

import java.util.Map;

import org.vaadin.aboutbox.client.ui.VAboutBox;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;

/**
 * Server side component for the VAboutBox widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.aboutbox.client.ui.VAboutBox.class)
public class AboutBox extends AbstractComponent {

    private static final long serialVersionUID = 3838190603396285099L;
    private static final String VERSION = "1.0.0";

    public static final String DEFAULT_STYLE = VAboutBox.STYLE_DEFAULT;
    private static final String DEFAULT_TITLE = "About<br />" + VERSION;
    private static final String DEFAULT_CAPTION = "About";
    private static final String DEFAULT_DESCRIPTION = "";
    private static final String DEFAULT_LINK_CAPTION = null;
    private static final Alignment DEFAULT_ALIGNMENT = Alignment.TOP_RIGHT;
    private static final int DEFAULT_OFFSET = 100;
    private String linkCaption;
    private String title;
    private ClickListener listener;
    private Alignment alignment;
    private int offset;
    private boolean open;
    private boolean sendOpen;

    /** Listener callback interface */
    public interface ClickListener {

        /** Invoked when user clicks the about box link. */
        public void aboutBoxClicked(AboutBox sender);

    }

    /**
     * Create new AboutBox.
     *
     */
    public AboutBox() {
        setTitle(DEFAULT_TITLE);
        setCaption(DEFAULT_CAPTION);
        setDescription(DEFAULT_DESCRIPTION);
        setLinkCaption(DEFAULT_LINK_CAPTION);
        setAlignment(DEFAULT_ALIGNMENT);
        setOffset(DEFAULT_OFFSET);
        setStyleName(DEFAULT_STYLE);
    }

    /**
     * Create new AboutBox.
     *
     */
    public AboutBox(String title, String description) {
        this();
        setTitle(title);
        setCaption(DEFAULT_CAPTION);
        setDescription(DEFAULT_DESCRIPTION);
        setLinkCaption(DEFAULT_LINK_CAPTION);
    }

    /**
     * Create new AboutBox
     *
     * @param caption
     * @param title
     * @param description
     * @param linkCaption
     * @param linkListener
     */
    public AboutBox(String caption, String title, String description,
            String linkCaption, ClickListener linkListener) {
        this(title, description);
        setCaption(caption);
        setLinkCaption(linkCaption);
        setListener(linkListener);
        setStyleName(DEFAULT_STYLE);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("boxStyle", getStyleName());
        target.addAttribute("title", getTitle());
        if (getLinkCaption() != null && !"".equals(linkCaption)) {
            target.addAttribute("linkCaption", getLinkCaption());
        }
        target.addAttribute("align", getAlignment().getBitMask());
        target.addAttribute("offset", offset);
        if (sendOpen) {
            target.addAttribute("open", open);
        }
        sendOpen = false;
    }

    /**
     * Receive and handle events and other variable changes from the client.
     *
     * {@inheritDoc}
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        // Variables set by the widget are returned in the "variables" map.

        if (variables.containsKey(VAboutBox.CLICK_EVENT_IDENTIFIER)) {
            if (getListener() != null) {
                getListener().aboutBoxClicked(this);
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
        requestRepaint();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description != null ? description
                : DEFAULT_DESCRIPTION);
    }

    public void setLinkCaption(String linkCaption) {
        this.linkCaption = linkCaption;
        requestRepaint();
    }

    public String getLinkCaption() {
        return linkCaption;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public ClickListener getListener() {
        return listener;
    }

    @Override
    public void setStyleName(String style) {
        super.setStyleName(style != null ? style : DEFAULT_STYLE);
    }

    public void open() {
        open = true;
        sendOpen = true;
        requestRepaint();
    }

    public void close() {
        open = false;
        sendOpen = true;
        requestRepaint();
    }

    public void setOffset(int newOffset) {
        offset = newOffset <= 0 ? 0 : newOffset;
        requestRepaint();
    }

    public int getOffset() {
        return offset;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment != null ? alignment : DEFAULT_ALIGNMENT;
        requestRepaint();
    }

    public Alignment getAlignment() {
        return alignment;
    }

}
