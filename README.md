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

To download locally all the javadocs, simply run all the commands in the correct order into your terminal.

After downloading all the javadocs your default browser will open a new window to the index.html (```~\Spigot-JavaDoc-Downloader\javadocs```).

### Windows & Linux
    git clone https://github.com/SadShrimpyy/Spigot-JavaDoc-Downloader.git
    mkdir Spigot-JavaDoc-Downloader/build
    cd Spigot-JavaDoc-Downloader/src
    javac -d ../build Main.java && cd .. && java -cp build Main