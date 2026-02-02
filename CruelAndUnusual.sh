#!/bin/bash
if [[ $(uname) == "Linux" ]]; then
  java  -jar ./cruelandunusual/target/cruelandunusual*-jar-with-dependencies.jar
elif [[ $(uname) == "MacOS" ]]; then
  java  -XstartOnFirstThread -jar ./cruelandunusual/target/cruelandunusual*-jar-with-dependencies.jar
fi
