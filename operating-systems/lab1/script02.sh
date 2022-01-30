#!/bin/bash

ans=""

while true
do
  read line
  if [[ "$line" == "q" ]]
  then break
  else ans+="$line"
  fi
done

echo "$ans"