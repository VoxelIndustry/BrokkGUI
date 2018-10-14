# BrokkGUI [![Build Status](https://img.shields.io/travis/VoxelIndustry/BrokkGUI.svg?style=flat-square)](https://travis-ci.org/VoxelIndustry/BrokkGUI)

_A powerful generalist UI framework._

BrokkGUI is a complete framework for making GUIs in Minecraft mods. It features a CSS parser/engine, clear data bindings and an extensive collection of base components.

**The framework is currently in beta and will soon release in 1.0.0**

# Depending on BrokkGUI
The framework is composed of several projects. You should use one of the available bindings since the core project is not usable alone.
BrokkGUI depends on two additional libraries not present in Minecraft :
* [Teabeans](https://github.com/Ourten/TeaBeans) used for the bindings and properties.
* [Hermod](https://github.com/VoxelIndustry/Hermod) used for the event system.

To depends on a binding add the following to your gradle build file :
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