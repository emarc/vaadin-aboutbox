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
    private String title;
    private String linkCaption;
    private ClickListener listener;
    private Alignment alignment;
    private int offset;

    /** Listener callback interface */
    public interface ClickListener {

        /** Invoked when user clicks the about box link. */
        public void aboutBoxClicked(AboutBox sender);

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
        super();
        setCaption(caption);
        setTitle(title);
        setDescription(description);
        setLinkCaption(linkCaption);
        setListener(linkListener);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("style", getStyleName());
        target.addAttribute("title", getTitle());
        target.addAttribute("linkCaption", getLinkCaption());
        target.addAttribute("align", alignment.getBitMask());
        target.addAttribute("offset", offset);
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
        super.setStyleName(style);
    }

}
