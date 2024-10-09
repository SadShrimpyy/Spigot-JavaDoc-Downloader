# Spigot JavaDocs Downloader

This is a simple Java 17 project that allows users to download all the JavaDocs for [Spigot](https://www.spigotmc.org/), a popular Minecraft server API.

This tool streamlines the process of obtaining the JavaDocs from the [Sonatype Nexus Repository](https://hub.spigotmc.org/nexus/#browse/browse:public), making it easier for developers to reference the documentation offline.

## Features

- Downloads all JavaDocs for the Spigot API
- Simple command-line interface
- Written in Java 17

## Prerequisites

- Java 17 or higher installed on your machine (and set to your PATH)

## Installation

You can either:

### Download the .jar and run
To download locally all the javadocs, simply download the [latest release](https://github.com/SadShrimpyy/Spigot-JavaDoc-Downloader/releases/download/v1.0/spigot-api-javadocs-downloader-v1.0.jar) and execute the jar with ```java -jar spigot-api-javadocs-downloader-v1.0.jar```.

After downloading all the javadocs your default browser will open a new window to the index.html (```~\javadocs```).

### Clone the repository, compile, and run
To download locally all the javadocs, simply run all the commands in the correct order into your terminal.

After downloading all the javadocs your default browser will open a new window to the index.html (```~\Spigot-JavaDoc-Downloader\javadocs```).

    git clone https://github.com/SadShrimpyy/Spigot-JavaDoc-Downloader.git
    mkdir Spigot-JavaDoc-Downloader/build
    cd Spigot-JavaDoc-Downloader/src
    javac -d ../build Main.java && cd .. && java -cp build Main

## TODO List / Known issues
1. As now if you run more than once, this tool will download again all the javadocs. If you have already, just skip!
