# AboutBox

A collapsible "About" panel that sticks to the top edge of the viewport. Click the tab to expand it and reveal a title, description, and optional link.

Three built-in themes: **leather** (default), **vaadin**, **brushed-metal**.

## Requirements

- Java 21
- Vaadin 25

## Usage

```java
AboutBox box = new AboutBox(
    "About",                          // tab label
    "My App<br/>v1.0",               // title (HTML)
    "A short description.",           // body (HTML)
    "Learn more",                     // link label (null = no link)
    sender -> Notification.show("clicked!")
);
add(box);
```

## Build the add-on

```bash
mvn install -pl aboutbox-addon
```

## Run the demo

```bash
mvn spring-boot:run -pl aboutbox-demo
```

Open http://localhost:8080

## Build a Vaadin Directory release package

```bash
mvn -pl aboutbox-addon -P directory clean package
```

The zip ready for upload is at `aboutbox-addon/target/aboutbox-addon-<version>.zip`.

## Changelog

Version 1.0
-----------
- Configurable Properties
    - Caption - The visible caption in collapsed mode, 1 line, text-only
    - Title - Caption visible when opened, 2 lines, HTML
    - Description - Application description visible when opened, 3 lines,
    - More Caption - Caption for 'show more' link, 1 line text-only
    - Alignment - From left, right edge (using Vaadin's Alignment class)
    - Distance - Distance from the alignment edge
- Available Themes
    - leather (default) - Leather look-a-like
    - vaadin - Basic Vaadin-style rounded box
- Implementation Details
    - Fonts are defined as part of CSS
    - Only for top edge of the screen
    - Size: 140x260 visible, 16x290 shadowed
    - Can only be added to main window (check for this?)
    - Long texts will be cut/hidden
    - Browsers: Webkit-based, FF3.6+, IE7+

Version 2.0
-----------
- Configurable Properties
    - Alignment - From left, right, top, bottom edge (using Vaadin's Alignment class)
- Available Themes
    - brushed-metal - A sheet of brushed metal
    - paper - Plain white paper sheet
