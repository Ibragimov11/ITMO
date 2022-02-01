#!/bin/bash

echo "" > report2.log
array=()
cnt=0

while true
do
  array+=(1 2 3 4 5 6 7 8 9 10)
  cnt=$(($cnt+1))
  if [ $((cnt % 100000)) -eq 0 ]
  then
    echo "size: ${#array[*]}" > report2.log
  fi
done
