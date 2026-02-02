#!/bin/bash
if [[ $(uname) == "Linux" ]]; then
  java -Djava.library.path=~/.m2/repository/org/lwjgl/lwjgl/3.4.0/ -jar ./cruelandunusual/target/cruelandunusual*.jar
elif [[ $(uname) == "MacOS" ]]; then
  java -Djava.library.path=~/.m2/repository/org/lwjgl/lwjgl/3.4.0/ -XstartOnFirstThread -jar ./cruelandunusual/target/cruelandunusual*.jar
fi
