package org.vaadin.aboutbox;

/**
 * Built-in visual themes for the AboutBox component.
 */
public enum AboutBoxVariant {

    LEATHER("leather"),
    VAADIN("vaadin"),
    BRUSHED_METAL("brushed-metal");

    private final String variantName;

    AboutBoxVariant(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantName() {
        return variantName;
    }
}
