#!/bin/bash
VERBOSE=false
if [[ $1 == "verbose" ]]; then
  VERBOSE=true
  shift
fi
if [[ $1 == "help" ]]; then
  echo "Cruel and Unusual Command Line:"
  echo "Usage: CruelAndUnusual [subcommands...]"
  echo "Subcommands(Can be specify multiple, they are processed in the following order)"
  echo "verbose - Displays the commands being run"
  echo "help - displays this message"
  echo "cleanCrashLogs - Cleans crash logs"
  echo "build - Builds Cruel and Unusual"
  echo "build test - Builds Cruel and Unusual and continues processing commands"
  echo "server {name} - starts the game as a server. Equivalent to headless world {name}"
  echo "server new {name} {size} {RulesFile} - starts the game as a server and generates a new world."
  echo "noRun - Prevents starting the game. Useful for running other features without starting the game"
  echo "The following subcommands are processed in any order"
  echo "genWorld {name} {size} {RulesFile} - Generates a world named name size KM square following RulesFile"
  echo "version - Prints version info and exists"
  echo "headless - Runs the game without the gl instance. Intended for servers"
  echo "world {name} - Starts the game with the named world"
  exit
fi
if [[ $1 == "cleanCrashLogs" ]]; then
  $VERBOSE && echo "rm *.log"
  rm *.log
  shift
fi
if [[ $1 == "build" ]]; then
  $VERBOSE && echo "Entering build directory"
  cd cruelandunusual || echo "can not locate build directory"
  $VERBOSE && echo "mvn package"
  mvn package || echo "build failed"
  $VERBOSE && echo "Exiting build directory"
  cd ..
  shift
  [[ $1 == "test" ]] || exit;
  shift
fi
if [[ $1 == "server" ]]; then
  shift
  OPTS="headless"
  if [[ $1 == "new" ]]; then
    $VERBOSE && echo "setting options: headless genWorld $2 $3 $4 world $2"
    OPTS="$OPTS genWorld $2 $3 $4 world $2"
    shift 4
  else
    $VERBOSE && echo "setting options: headless world $1"
    OPTS="$OPTS world $1"
    shift
  fi
fi
OPTS="$OPTS $@"
[[ $1 == "noRun" ]] && exit
[[ $1 == "DEBUG" ]] && DEBUG=" -Dorg.lwjgl.util.Debug=true"
$VERBOSE && echo "Starting jar with options: $OPTS"
if [[ $(uname) == "Linux" ]]; then
  $VERBOSE && echo "java command:" java  -jar ./cruelandunusual/target/cruelandunusual*-jar-with-dependencies.jar $OPTS
  java$DEBUG  -jar ./cruelandunusual/target/cruelandunusual*-jar-with-dependencies.jar $OPTS
elif [[ $(uname) == "MacOS" ]]; then
  $VERBOSE && echo "java command:" java -XstartOnFirstThread -jar ./cruelandunusual/target/cruelandunusual*-jar-with-dependencies.jar $OPTS
  java  -XstartOnFirstThread -jar ./cruelandunusual/target/cruelandunusual*-jar-with-dependencies.jar $OPTS
fi
