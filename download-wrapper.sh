#!/bin/bash
# Download the Gradle wrapper JAR

mkdir -p gradle/wrapper

cd gradle/wrapper

# Download gradle-wrapper.jar
echo "Downloading gradle-wrapper.jar..."
curl -L https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar -o gradle-wrapper.jar

if [ -f gradle-wrapper.jar ]; then
    echo "✅ gradle-wrapper.jar downloaded successfully"
    ls -lh gradle-wrapper.jar
else
    echo "❌ Failed to download gradle-wrapper.jar"
    exit 1
fi
