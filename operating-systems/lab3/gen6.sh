#!/bin/bash

while true
do
  read string
  case $string in
    "TERM")
      kill -TERM $(cat pid6)
      exit
      ;;
    "+")
      kill -USR1 $(cat pid6)
      ;;
    "*")
      kill -USR2 $(cat pid6)
      ;;
    "-")
      kill -USR3 $(cat pid6)
      ;;
    *)
      :
      ;;
  esac
done