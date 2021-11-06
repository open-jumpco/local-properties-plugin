# Gradle Local Properties Plugin

[![Install](https://img.shields.io/badge/install-plugin-brown.svg)](https://plugins.gradle.org/plugin/io.jumpco.open.gradle.local-properties)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)

This plugin provides for local properties to override gradle.properties.
By creating a file named `gradle.local.properties` and updating properties these properties can be modified for local development.

A warning will be logged if the property in `gradle.local.properties` is a new property. 

## Setup

Add the following to your build.gradle file:

```groovy
plugins {
    id 'io.jumpco.open.gradle.local-properties' version '1.0.0'
}
```

## Versioning

See [gradle plugin page](https://plugins.gradle.org/plugin/io.jumpco.open.gradle.local-properties) for other versions.

# Usage

Create a file name `gradle.local.properties` within a project folder in order to override properties.

Ideally this file is added to `.gitignore` and not committed to your repository.
