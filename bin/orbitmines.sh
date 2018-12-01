#!/bin/bash

#
# Presets
#

BLACK='\033[0;30m'
RED='\033[0;31m'
GREEN='\033[0;32m'
ORANGE='\033[0;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
LIGHT_GRAY='\033[0;37m'
DARK_GRAY='\033[1;30m'
LIGHT_RED='\033[1;31m'
LIGHT_GREEN='\033[1;32m'
YELLOW='\033[1;33m'
LIGHT_BLUE='\033[1;34m'
LIGHT_PURPLE='\033[1;35m'
LIGHT_CYAN='\033[1;36m'
WHITE='\033[1;37m'
RESET='\033[0m'

PREFIX=${RESET}[${DARK_GRAY}OrbitMines${RESET}]${WHITE}

#
# MySQL
#

mysql_hostname='root'
mysql_password='password'
mysql_database='OrbitMines'
mysql='mysql -u '$mysql_hostname' -p'${mysql_password}' -D '${mysql_database}
query=$mysql' -e '
query_se=$mysql' -es '

#
# Load MySQL
#

servers=()
index=0
for server in $($query"SELECT Server FROM Servers"); do
  if [ $index == 0 ]; then
    servers[$index]="BUNGEE"
  else
    servers[$index]=$server
  fi

  let index++
done

#
# Servers
#

function start_server() {
  if running $1; then
	echo -e $PREFIX $ORANGE$1' is already running.'
  else
	echo -e $PREFIX $LIGHT_GREEN'Starting '$1'...'$RESET
    $(cd /home/orbitmines/$1 ; screen -dmS $1 sh start.sh ; cd /home/orbitmines)
    sleep 1
  fi
}

function stop_server() {
  if running $1; then
    echo -e $PREFIX $LIGHT_RED'Stopping '$1'...'$RESET
    screen -X -S $1 kill
    sleep 2
  else
	echo -e $PREFIX $LIGHT_RED$1' is not running. Start it with '$CYAN'orbitmines start '$1$LIGHT_RED'.'
  fi
}

function restart_server() {
  stop_server $1
  start_server $1
}

function open_console() {
  if running $1; then
    screen -r $1
  else
	echo -e $PREFIX $LIGHT_RED$1' is not running. Start it with '$CYAN'orbitmines start '$1$LIGHT_RED'.'
  fi
}

function exists() {
  for server in "${servers[@]}"; do
    if [ "$server" = "$1" ]; then
      return 0;
    fi
  done

  echo -e $PREFIX 'The server '$1' does not exist.'
  return 1;
}

function running() {
  if screen -list | grep -q $1; then
    return 0;
  else
    return 1;
  fi
}

server=''
case $1 in
  start)
    case $2 in
      all)
        echo -e $PREFIX 'Starting all servers...'
        for server in "${servers[@]}"; do
          start_server $server
        done
        echo -e $PREFIX 'Started all servers.'
      ;;
      *)
        if [ $# -eq 2 ]; then
          server=${2^^}
          if exists $server; then
            start_server $server
          fi
        else
          echo -e $PREFIX $RED'Unknown server.'
        fi
      ;;
    esac
  ;;
  stop)
    case $2 in
      all)
        echo -e $PREFIX 'Stopping all servers...'
        for server in "${servers[@]}"; do
          stop_server $server
        done
        echo -e $PREFIX 'Stopped all servers.'
      ;;
      *)
        if [ $# -eq 2 ]; then
          server=${2^^}
          if exists $server; then
            stop_server $server
          fi
        else
          echo -e $PREFIX $RED'Unknown server.'
        fi
      ;;
    esac
  ;;
  restart)
    case $2 in
      all)
        echo -e $PREFIX 'Restarting all servers...'
        for server in "${servers[@]}"; do
          restart_server $server
        done
        echo -e $PREFIX 'Restarted all servers.'
      ;;
      *)
        if [ $# -eq 2 ]; then
          server=${2^^}
          if exists $server; then
            restart_server $server
            open_console $server
          fi
        else
          echo -e $PREFIX $RED'Unknown server.'
        fi
      ;;
    esac
  ;;
  console)
    if [ $# -eq 2 ]; then
      if exists ${2^^}; then
        open_console ${2^^}
      fi
    else
      echo -e $PREFIX 'Use:' $CYAN'orbitmines console (server)' $RESET'(Open a console, exit with Ctr+A+D)'
    fi
  ;;
  *)
  echo ''
  echo -e $PREFIX $WHITE'Available Servers'
  for server in "${servers[@]}"; do
    if running $server; then
      echo -e $PREFIX '  ' $LIGHT_GREEN$server $RESET'(Open with '$CYAN'orbitmines console '$server$RESET', exit with Ctr+A+D)'
    else
      echo -e $PREFIX '  ' $RED$server $RESET' (Start with '$CYAN'orbitmines start '$server$RESET')'
    fi
  done
  echo ''
  echo -e $PREFIX $WHITE'Command Help'
  echo -e $PREFIX '  ' $CYAN'orbitmines start (server)|all' $RESET'(Start a server)'
  echo -e $PREFIX '  ' $CYAN'orbitmines stop (server)|all' $RESET'(Stop a server)'
  echo -e $PREFIX '  ' $CYAN'orbitmines restart (server)|all' $RESET'(Restart a server)'
  echo -e $PREFIX '  ' $CYAN'orbitmines console (server)' $RESET'(Open a console, exit with Ctr+A+D)'
  ;;
esac


echo -e $RESET
