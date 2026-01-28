#!/bin/bash
java -Djava.library.path=~/.m2/repository/org/lwjgl/lwjgl/3.3.4/macos/arm64/org/lwjgl/liblwjgl.dylib 
-XstartOnFirstThread -jar ./cruelandunusual/target/CruelAndUnusual*.jar
