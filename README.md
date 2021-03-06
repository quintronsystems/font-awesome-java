Font Awesome Java
===
Version: 1.0.0

## Example Usage
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

## How to use
### Project
1. clone this repo
1. Open this project in your IDE of choice.  Settings for NetBeans and Intellij are included.
1. (Optional) run the import script for Font-Awesome-Pro
1. Build the jar
1. Import the jar OR this project into your project.

### Jar
#### [Download the latest free jar](dist/font-awesome.jar)

Include this jar into your project as a library

## Import Instructions

Use the importer to import new versions of Font Awesome, or import Font Awesome Pro.

**Important:** If you're importing Font Awesome Pro, this import script will need to be ran almost every time you update
this project from the git, as this repo will be kept up to date with the latest free version.

The importer uses git submodules and a node.js script to process the import. 

1. Check out submodule for desired version of Font Awesome
    1. Change Directory to submodule: `cd Font-Awesome`
    1. Pull `git pull`
    1. Change back to project directory: `cd ..`
1. Run `npm install`
1. Run import script `node import.js`
1. Choose Pro or Free
1. If free, enter the version of Font-Awesome you are importing 


Todo
---
1. Add JavaDoc commenting to code and generate JavaDoc
1. Add Images to README