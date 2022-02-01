#!/bin/bash

result=""
max=0

for pid in $(ps -A -o pid --no-header)
do
  file=/proc/$pid/status
  if [[ -f $file ]]
  then
    size=$(grep VmSize $file | awk '{print $2}')
    if [[ $size -gt $max ]]
    then
      max=$size
      result=$pid
    fi
  fi
done

echo 'PID='$result' SIZE='$max' kB' > ans6
