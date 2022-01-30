#!/bin/bash

rm ans5
touch ans5

N=0
M=0
count=0

while read line
do
  ppid=$(echo $line | grep -E -o 'Parent_ProcessID= [[:digit:]]+' | awk '{print $2}')
  if [[ $N -eq $ppid ]]
  then
    count=$count+1
    time=$(echo $line | grep -E -o 'Average_Running_Time= [[:digit:]]+,[[:digit:]]*' | awk '{print $2}')
    M=$(echo $M $time | awk '{print $1+$2}')
  else
    M=$(echo $M count | awk '{print $1/$2}')
    echo 'Average_Running_Children_of_ParentID='$N' is '$M >> ans5
    N=$(echo $line | grep -E -o 'Parent_ProcessID= [[:digit:]]+' | awk '{print $2}')
    M=$(echo $line | grep -E -o 'Average_Running_Time= [[:digit:]]+,[[:digit:]]*' | awk '{print $2}')
    count=1
  fi
  echo $line >> ans5
done < ans4

M=$(echo $M $count | awk '{print $1/$2}')
echo 'Average_Running_Children_of_ParentID='$N' is '$M >> ans5