#!/bin/bash

echo $$ > pid6

operation="+"
res=1

sigterm() {
  echo "stopped by sigterm"
  exit
}

usr1() {
  operation="+"
}

usr2() {
  operation="*"
}

usr3() {
  operation="-"
}

trap 'sigterm' SIGTERM
trap 'usr1' USR1
trap 'usr2' USR2
trap 'usr3' USR3

while true
do
  case $operation in
    "+")
      let res=$res+2
      ;;
    "*")
      let res=$res*2
      ;;
    "-")
      let res=$res-2
      ;;
    *)
      :
      ;;
  esac
  echo $res
  sleep 1
done