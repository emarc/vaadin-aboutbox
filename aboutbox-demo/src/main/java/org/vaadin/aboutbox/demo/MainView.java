package org.vaadin.aboutbox.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import org.vaadin.aboutbox.AboutBox;
import org.vaadin.aboutbox.AboutBoxVariant;
import org.vaadin.aboutbox.AlignmentSide;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        setPadding(true);
        setSpacing(true);

        add(new H1("AboutBox Demo"));
        add(new Paragraph(
            "This page demonstrates the AboutBox add-on for Vaadin 25. " +
            "Look for the sticky tab at the top edge of the viewport."
        ));

        // ------------------------------------------------------------------
        // Primary box — leather theme, right-aligned (default)
        // ------------------------------------------------------------------
        AboutBox leatherBox = new AboutBox(
            "About",
            "My Application<br/>v2.0",
            "A demo showing the AboutBox Vaadin 25 add-on.<br/>Three themes, two alignments.",
            "Learn more...",
            sender -> Notification.show("Link clicked!", 3000, Notification.Position.BOTTOM_CENTER)
        );
        leatherBox.setAlignment(AlignmentSide.RIGHT);
        leatherBox.setOffset(120);
        add(leatherBox);

        // ------------------------------------------------------------------
        // Theme switcher
        // ------------------------------------------------------------------
        add(new H2("Controls for the right-side box"));

        Select<String> themeSelect = new Select<>();
        themeSelect.setLabel("Theme");
        themeSelect.setItems("leather", "vaadin", "brushed-metal");
        themeSelect.setValue("leather");
        themeSelect.addValueChangeListener(e -> leatherBox.setStyleName(e.getValue()));

        Select<String> alignSelect = new Select<>();
        alignSelect.setLabel("Alignment");
        alignSelect.setItems("Right", "Left");
        alignSelect.setValue("Right");
        alignSelect.addValueChangeListener(e ->
            leatherBox.setAlignment("Left".equals(e.getValue()) ? AlignmentSide.LEFT : AlignmentSide.RIGHT)
        );

        Button openBtn = new Button("Open", e -> leatherBox.open());
        Button closeBtn = new Button("Close", e -> leatherBox.close());

        HorizontalLayout controls = new HorizontalLayout(themeSelect, alignSelect, openBtn, closeBtn);
        controls.setAlignItems(Alignment.END);
        add(controls);

        // ------------------------------------------------------------------
        // Second box — vaadin theme, left-aligned, no link
        // ------------------------------------------------------------------
        add(new H2("Second box (left, Vaadin theme)"));
        add(new Paragraph("A second AboutBox with the 'vaadin' theme anchored to the left side."));

        AboutBox vaadinBox = new AboutBox(
            "Info",
            "Vaadin Theme",
            "This box uses the built-in Vaadin theme variant.",
            null,
            null
        );
        vaadinBox.setAlignment(AlignmentSide.LEFT);
        vaadinBox.setOffset(80);
        vaadinBox.setThemeVariant(AboutBoxVariant.VAADIN);
        add(vaadinBox);

        // ------------------------------------------------------------------
        // Third box — brushed-metal theme, right-aligned, different offset
        // ------------------------------------------------------------------
        add(new H2("Third box (right, Brushed Metal theme)"));
        add(new Paragraph("A third AboutBox with the 'brushed-metal' theme, offset 300px from the right."));

        AboutBox metalBox = new AboutBox(
            "Help",
            "Brushed Metal<br/>Theme",
            "Demonstrates the brushed-metal visual style.",
            "Open docs",
            sender -> Notification.show("Docs link clicked!")
        );
        metalBox.setAlignment(AlignmentSide.RIGHT);
        metalBox.setOffset(300);
        metalBox.setThemeVariant(AboutBoxVariant.BRUSHED_METAL);
        add(metalBox);
    }
}
