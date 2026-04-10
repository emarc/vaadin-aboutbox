import { LitElement, html, unsafeCSS } from 'lit';
import { customElement, property, state } from 'lit/decorators.js';
import { unsafeHTML } from 'lit/directives/unsafe-html.js';
import { styleMap } from 'lit/directives/style-map.js';

// ?inline makes Vite process the CSS file (resolving url() paths to assets)
// and return it as a plain string for use with unsafeCSS().
import styles from './about-box.css?inline';

/**
 * `<about-box>` — A collapsible "About" information panel that anchors to
 * the top edge of the viewport. Clicking the tab expands it to reveal a
 * title, description, and optional action link.
 *
 * @attr {string} theme - Visual theme: "leather" (default), "vaadin", "brushed-metal"
 * @attr {boolean} alignright - When present, panel anchors to right edge (default)
 * @attr {boolean} opened - When present, panel is expanded
 *
 * @fires opened-changed - Fired when the panel opens or closes (two-way binding)
 * @fires about-box-link-clicked - Fired when the user clicks the link
 */
@customElement('about-box')
export class AboutBox extends LitElement {

    /** HTML content shown as the main title when expanded. */
    @property({ type: String }) boxTitle: string = '';

    /** Plain-text label shown on the collapsed tab. */
    @property({ type: String }) caption: string = 'About';

    /** HTML content shown as the body text when expanded. */
    @property({ type: String }) description: string = '';

    /** Plain-text label for the action link. Hidden when empty. */
    @property({ type: String }) linkCaption: string = '';

    /** When true, the panel anchors to the right edge of the viewport. */
    // Use attribute: 'alignright' (no hyphen) because Vaadin Flow serialises
    // the Java property name "alignRight" as the lowercase attribute "alignright",
    // not the kebab-case "align-right" that Lit would generate by default.
    @property({ type: Boolean, reflect: true, attribute: 'alignright' }) alignRight: boolean = true;

    /** Distance in pixels from the anchored edge. */
    @property({ type: Number }) offset: number = 100;

    /** When true, the panel is expanded. */
    @property({ type: Boolean, reflect: true }) opened: boolean = false;

    @state() private _highlighted: boolean = false;

    static styles = unsafeCSS(styles);

    override connectedCallback() {
        super.connectedCallback();
        // @import is not allowed in shadow DOM style elements, so load the
        // Tangerine font (used by the leather theme) into the document <head>.
        if (!document.querySelector('link[href*="Tangerine"]')) {
            const link = document.createElement('link');
            link.rel = 'stylesheet';
            link.href = 'https://fonts.googleapis.com/css2?family=Tangerine:wght@700&display=swap';
            document.head.appendChild(link);
        }
    }

    render() {
        const boxClasses = [
            'about',
            this.opened ? 'open' : '',
            this._highlighted ? 'hl' : '',
        ].filter(Boolean).join(' ');

        // Offset is applied from the anchored edge; box is 10px closer than shadow
        const boxOffset = Math.max(0, this.offset - 10);
        const boxStyle = this.alignRight
            ? styleMap({ right: `${boxOffset}px` })
            : styleMap({ left: `${boxOffset}px` });

        return html`
            <div
                class=${boxClasses}
                style=${boxStyle}
                @click=${this._handleClick}
                @mouseenter=${this._handleMouseEnter}
                @mouseleave=${this._handleMouseLeave}
            >
                <div class="title">${unsafeHTML(this.boxTitle)}</div>
                <div class="description">${unsafeHTML(this.description)}</div>
                ${this.linkCaption
                    ? html`<div class="link" @click=${this._handleLinkClick}>${this.linkCaption}</div>`
                    : html`<div class="link"></div>`
                }
                <div class="caption">${this.caption}</div>
            </div>
        `;
    }

    private _handleClick(e: MouseEvent) {
        // Only toggle when clicking the box itself, not the link (which has its own handler)
        const target = e.composedPath()[0] as HTMLElement;
        if (!target.classList.contains('link')) {
            this._toggle();
        }
    }

    private _handleLinkClick(e: MouseEvent) {
        e.stopPropagation();
        if (this.linkCaption) {
            this.dispatchEvent(new CustomEvent('about-box-link-clicked', {
                bubbles: true,
                composed: true,
            }));
        }
    }

    private _handleMouseEnter() {
        if (!this.opened) {
            this._highlighted = true;
        }
    }

    private _handleMouseLeave() {
        this._highlighted = false;
    }

    private _toggle() {
        this.opened = !this.opened;
        // Notify Vaadin Flow server of state change (two-way binding)
        this.dispatchEvent(new CustomEvent('opened-changed', {
            detail: { value: this.opened },
            bubbles: true,
            composed: true,
        }));
    }
}
