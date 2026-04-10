package org.vaadin.aboutbox;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;

import java.util.Objects;

/**
 * AboutBox is a collapsible information panel that anchors to the top edge of
 * the viewport. It starts collapsed showing only a small caption tab; clicking
 * expands it to reveal a title, description, and an optional action link.
 *
 * <p>Upgraded from Vaadin 6 GWT-based add-on to Vaadin 25 Lit web component.
 *
 * <h2>Basic usage</h2>
 * <pre>{@code
 * AboutBox box = new AboutBox(
 *     "About",
 *     "My App<br/>v2.0",
 *     "A great application.",
 *     "Learn more",
 *     sender -> Notification.show("Link clicked!")
 * );
 * add(box);
 * }</pre>
 */
@Tag("about-box")
@JsModule("./about-box.ts")
public class AboutBox extends Component {

    private static final String DEFAULT_TITLE = "About";
    private static final String DEFAULT_CAPTION = "About";
    private static final String DEFAULT_DESCRIPTION = "";

    /**
     * Legacy Vaadin 6 style listener kept for API compatibility.
     */
    private ClickListener legacyListener;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Creates an AboutBox with the default title, caption, description,
     * alignment, offset, and theme.
     */
    public AboutBox() {
        setTitle(DEFAULT_TITLE);
        setCaption(DEFAULT_CAPTION);
        setDescription(DEFAULT_DESCRIPTION);
        setAlignment(AlignmentSide.RIGHT);
        setOffset(100);
        setThemeVariant(AboutBoxVariant.LEATHER);
        wireLegacyListener();
    }

    /**
     * Creates an AboutBox with a custom title and description.
     *
     * @param title the HTML content shown in the expanded title area
     * @param description the HTML content shown in the expanded description area
     */
    public AboutBox(String title, String description) {
        this();
        setTitle(title);
        setDescription(description);
    }

    /**
     * Creates an AboutBox with custom caption, title, description, and link
     * listener.
     *
     * @param caption the collapsed tab caption
     * @param title the HTML content shown in the expanded title area
     * @param description the HTML content shown in the expanded description area
     * @param linkCaption the text shown for the optional action link
     * @param listener the legacy click listener invoked when the link is clicked
     */
    public AboutBox(String caption, String title, String description,
                    String linkCaption, ClickListener listener) {
        this(title, description);
        setCaption(caption);
        setLinkCaption(linkCaption);
        setListener(listener);
    }

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    /**
     * Sets the title HTML displayed when the AboutBox is expanded.
     *
     * @param html the title HTML, or {@code null} to clear it
     */
    public void setTitle(String html) {
        getElement().setProperty("boxTitle", Objects.requireNonNullElse(html, ""));
    }

    /**
     * Gets the title HTML displayed in the expanded AboutBox.
     *
     * @return the configured title HTML
     */
    public String getTitle() {
        return getElement().getProperty("boxTitle", "");
    }

    /**
     * Sets the caption shown when the AboutBox is collapsed.
     *
     * @param text the caption text, or {@code null} to clear it
     */
    public void setCaption(String text) {
        getElement().setProperty("caption", Objects.requireNonNullElse(text, ""));
    }

    /**
     * Gets the collapsed caption text.
     *
     * @return the current caption text
     */
    public String getCaption() {
        return getElement().getProperty("caption", "");
    }

    /**
     * Sets the description HTML displayed when the AboutBox is expanded.
     *
     * @param html the description HTML, or {@code null} to clear it
     */
    public void setDescription(String html) {
        getElement().setProperty("description", Objects.requireNonNullElse(html, ""));
    }

    /**
     * Gets the description HTML displayed in the expanded AboutBox.
     *
     * @return the configured description HTML
     */
    public String getDescription() {
        return getElement().getProperty("description", "");
    }

    /**
     * Sets the caption of the optional action link.
     *
     * @param text the link caption, or {@code null} to clear it
     */
    public void setLinkCaption(String text) {
        getElement().setProperty("linkCaption", Objects.requireNonNullElse(text, ""));
    }

    /**
     * Gets the caption of the optional action link.
     *
     * @return the current link caption
     */
    public String getLinkCaption() {
        return getElement().getProperty("linkCaption", "");
    }

    /**
     * Sets which horizontal edge the AboutBox is anchored to.
     *
     * @param side the alignment side, or {@code null} to use
     *        {@link AlignmentSide#RIGHT}
     */
    public void setAlignment(AlignmentSide side) {
        getElement().setProperty("alignRight",
                side == null || side == AlignmentSide.RIGHT);
    }

    /**
     * Sets the top offset in pixels from the viewport edge.
     *
     * @param px the offset in pixels; negative values are clamped to {@code 0}
     */
    public void setOffset(int px) {
        getElement().setProperty("offset", Math.max(0, px));
    }

    /**
     * Gets the top offset in pixels.
     *
     * @return the current top offset
     */
    public int getOffset() {
        return (int) getElement().getProperty("offset", 100.0);
    }

    /**
     * Sets one of the built-in AboutBox theme variants.
     *
     * @param variant the theme variant, or {@code null} to use
     *        {@link AboutBoxVariant#LEATHER}
     */
    public void setThemeVariant(AboutBoxVariant variant) {
        getElement().setAttribute("theme",
                Objects.requireNonNullElse(variant, AboutBoxVariant.LEATHER).getVariantName());
    }

    /**
     * Sets the visual theme by name. Supported values: {@code "leather"},
     * {@code "vaadin"}, {@code "brushed-metal"}.
     * Preserved for API compatibility with the Vaadin 6 version.
     *
     * @param theme the theme name, or {@code null} to use the default theme
     */
    public void setStyleName(String theme) {
        getElement().setAttribute("theme",
                Objects.requireNonNullElse(theme, AboutBoxVariant.LEATHER.getVariantName()));
    }

    // -------------------------------------------------------------------------
    // Open / Close
    // -------------------------------------------------------------------------

    /**
     * Opens the AboutBox.
     */
    public void open() {
        getElement().setProperty("opened", true);
    }

    /**
     * Closes the AboutBox.
     */
    public void close() {
        getElement().setProperty("opened", false);
    }

    /**
     * Checks whether the AboutBox is currently open.
     *
     * @return {@code true} if the AboutBox is open, {@code false} otherwise
     */
    @Synchronize(property = "opened", value = "opened-changed")
    public boolean isOpen() {
        return getElement().getProperty("opened", false);
    }

    // -------------------------------------------------------------------------
    // Events
    // -------------------------------------------------------------------------

    /**
     * Listener interface preserved from the Vaadin 6 API.
     * Use {@link #addLinkClickListener} for the modern Flow event API.
     */
    public interface ClickListener {
        /**
         * Called when the AboutBox link is clicked.
         *
         * @param sender the AboutBox that fired the event
         */
        void aboutBoxClicked(AboutBox sender);
    }

    /**
     * Sets the legacy Vaadin 6 style click listener.
     *
     * @param listener the listener to register, or {@code null} to clear it
     */
    public void setListener(ClickListener listener) {
        this.legacyListener = listener;
    }

    /**
     * Gets the registered legacy click listener.
     *
     * @return the currently registered legacy listener, or {@code null}
     */
    public ClickListener getListener() {
        return legacyListener;
    }

    /**
     * Fired when the user clicks the link inside the expanded AboutBox.
     */
    @DomEvent("about-box-link-clicked")
    public static class LinkClickEvent extends ComponentEvent<AboutBox> {
        /**
         * Creates a new link click event.
         *
         * @param source the AboutBox that fired the event
         * @param fromClient whether the event originated from the client
         */
        public LinkClickEvent(AboutBox source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Adds a Flow event listener for link clicks.
     *
     * @param listener the listener to register
     * @return a registration handle that can be used to remove the listener
     */
    public Registration addLinkClickListener(ComponentEventListener<LinkClickEvent> listener) {
        return addListener(LinkClickEvent.class, listener);
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private void wireLegacyListener() {
        addListener(LinkClickEvent.class, event -> {
            if (legacyListener != null) {
                legacyListener.aboutBoxClicked(this);
            }
        });
    }
}
