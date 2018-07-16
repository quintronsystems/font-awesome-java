Font Awesome Java
===
Version: 1.0.0

Example Usage
---
```java

// Turn on Anti-Aliasing
FontAwesome.enableAntiAliasing();

// Initialize FontAwesome class
FontAwesome fa = new FontAwesome(16f); // Default Size: 16f

fa.setDefaultStyle(fa.REGULAR); // Default Style: Strong

fa.setDefaultColor(Color.red); // Default Color: Color.darkGrey

// Get Icon as JLabel
JLabel icon = fa.getIconLabel("arrow-alt-right");

// Get Icon as JButton
JButton button = fa.getIconButton("check");

// Get Icon as JButton that Toggles when Clicked
JButton toggleButton = fa.getIconToggleButton("check", "times");

// Get Icon as ImageIcon
ImageIcon imageIcon = fa.getIcon("check");

```

Todo
---
1. Add JavaDoc commenting to code and generate JavaDoc
2. Document Upgrade Process
3. Add Images to README