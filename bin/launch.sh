#!/bin/bash

# This script will launch the application using the default profile
$JAVA_HOME/bin/java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:FlightRecorderOptions=dumponexit=true,defaultrecording=true,dumponexitpath=/tmp/example.jfr -Dserver.port=8080 -jar build/libs/jvm-guy-rest-api-0.0.0-SNAPSHOT.jar
