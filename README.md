# BrokkGUI [![Build Status](https://img.shields.io/travis/VoxelIndustry/BrokkGUI.svg?style=flat-square)](https://travis-ci.org/VoxelIndustry/BrokkGUI) [![GitHub forks](https://img.shields.io/github/forks/voxelindustry/BrokkGUI.svg)](https://github.com/voxelindustry/BrokkGUI/network) [![GitHub issues](https://img.shields.io/github/issues/voxelindustry/BrokkGUI.svg)](https://github.com/voxelindustry/BrokkGUI/issues) [![GitHub license](https://img.shields.io/github/license/voxelindustry/BrokkGUI.svg)](https://github.com/voxelindustry/BrokkGUI/blob/master/LICENSE)

_A powerful generalist UI framework._

BrokkGUI is a complete framework for making GUIs in Minecraft mods. It features a CSS parser/engine, clear data bindings and an extensive collection of base components.

**The framework is currently in beta and will soon release in 1.0.0**

# Depending on BrokkGUI
The framework is composed of several projects. You should use one of the available bindings since the core project is not usable alone.
BrokkGUI depends on two additional libraries not present in Minecraft :
* [Teabeans](https://github.com/Ourten/TeaBeans) used for the bindings and properties.
* [Hermod](https://github.com/VoxelIndustry/Hermod) used for the event system.

To depend on a binding add the following to your gradle build file :
```gradle
repositories {
    maven {
      url 'http://maven.ferenyr.info/artifactory/libs-release
    }
}

dependencies {
   compile 'net.voxelindustry:brokkgui-binding-mc<minecraft version>:<version number>'
}
```

Since BrokkGUI is not currently released it is advised to shade it in your jar with the [gradle shadow plugin](https://github.com/johnrengelman/shadow).

# Bindings
The core project is platform agnostic and a binding must be created to control the rendering.

This allow the framework to work seamlessly between multiple minecraft versions and could work on other games or platforms.

There are several officials bindings bundled in this repository but the creation of third-party bindings is encouraged if you target a specific platform.

| Platform      | Status    | Future    |
| ------------- | --------- | --------- |
| Minecraft 1.7.10 | No container support | :warning:**Slated for removal** |
| Minecraft 1.11 | Full support | :warning:**Will be removed in 1.13** |
| Minecraft 1.12 | Full support | :white_check_mark:**Latest** |

# Features
#### CSS Engine
The framework support styling of GUIs and components with CSS code.

CSS styling make your UIs easily modifiable and more consistent with the use of global themes.

For example this code added to a global theme in your project will remove the default shadow of all text labels and color them like the Minecraft ones:
```css
label > text {
    shadow: false;
    color: #404040;
}
``` 
#### Abstraction layer
A GUI made with one binding will be seamlessly compatible with another if no specific features were used.
It allow a GUI to work on multiple Minecraft versions without hassle.

#### Reusable UI elements
The components of the framework combined with the CSS styling let you reuse the base components or your custom ones everywhere in a project.

#### Bindings and events
The use of TeaBeans enable a clear separation of the UI skin and style from the data and controls.

Multiple events have been added on the base components allowing you to react on hover, disable, click, text type, scroll and so on...

# Components
The framework contains the following base components:
* **Buttons**
  * GuiButton
  * GuiToggleButton
  * GuiCheckbox
  * GuiRadioButton
* **Labels**
  * GuiLabel
  * GuiLink
* **Inputs**
  * GuiTextfield
  * GuiTextfieldComplete _(Auto completed textfield)_
* **Panels and containers**  
  * GuiListView
  * ScrollPane
  * GuiAbsolutePane
  * GuiRelativePane
  * GuiTabPane
* **Windows**
  * GuiToast
  * GuiTooltip
  * SubGuiScreen

Components available only in Minecraft bindings:
* FluidStackView
* ItemStackView
* MCTooltip

# Examples
Theses examples are GUIs made with the framework and showcase some features combination.
### Inventory GUI with a scrollable nested window
<img src="http://i.ferenyr.info/images/storage/provider.gif" alt="Inventory GUI" width="393.5" height="402.5">

### GUI of a pressure regulator with complex shapes
<img src="http://i.ferenyr.info/images/storage/steamvent.gif" alt="Pressure GUI" width="354.5" height="334.5">

## Authors

* **[Ourten](https://github.com/Ourten)** - *Initial work*

See also the list of [contributors](https://github.com/voxelindustry/brokkgui/contributors) who participated in this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details