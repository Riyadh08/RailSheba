# RaiiSheba - Android Project 
# YouTube Video 
[![Watch the video](https://img.youtube.com/vi/4Y87LMED5E0/3.jpg)](https://www.youtube.com/watch?v=4Y87LMED5E0)

## Project Overview
**RaiiSheba** is a small Android application project developed using Android Studio. The project appears to be a Gradle-based Android application with standard project structure.

## Repository Structure

## File Details

### Configuration Files
- **.idea/** - Android Studio IDE configuration and metadata
- **.gitignore** - Git ignore file specifying files/folders to exclude from version control
- **build.gradle** - Project-level Gradle build configuration
- **settings.gradle** - Defines project modules and build settings
- **gradle.properties** - Gradle build properties and environment variables
- **gradlew** - Gradle wrapper executable for Unix/Linux systems
- **gradlew.bat** - Gradle wrapper executable for Windows systems

### Gradle Wrapper
- **gradle/wrapper/** - Contains Gradle wrapper JAR and properties
  - `gradle-wrapper.jar` - Gradle wrapper executable JAR
  - `gradle-wrapper.properties` - Wrapper configuration including Gradle version

### Application Module
- **app/** - Main application module containing:
  - Java source code
  - Android resources (layouts, strings, drawables)
  - AndroidManifest.xml
  - Module-specific build.gradle

## Development Information

### Contributors
- **Riyadh08** - Primary contributor with recent modifications

### Branch Information
- **Branches**: 2
- **Tags**: 0
- **Current Branch**: master

## Building the Project

### Prerequisites
- Android Studio (latest version recommended)
- Java Development Kit (JDK) 8 or higher
- Android SDK

### Build Commands
```bash
# Using Gradle wrapper (Unix/Linux/Mac)
./gradlew build

# Using Gradle wrapper (Windows)
gradlew.bat build

# Clean and build
./gradlew clean build

# Install debug APK to connected device
./gradlew installDebug


