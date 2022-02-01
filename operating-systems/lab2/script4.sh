#!/bin/bash

files=$(ls /proc)
touch tmp.txt

for file in $(echo "$files" | grep '[0-9]')
do
  pid=$(grep "^Pid:" "/proc/$file/status" | awk '{print $2}')
  ppid=$(grep "^PPid:" "/proc/$file/status" | awk '{print $2}')
  runtime=$(grep "se.sum_exec_runtime" "/proc/$file/sched" | awk '{print $3}')
  switches=$(grep "nr_switches" "/proc/$file/sched" | awk '{print $3}')
  art="undefined"
  if [ $switches -ne 0 ]
  then
    art=$(echo $runtime $switches | awk '{print $1/$2}')
  fi
  echo "ProcessID= $pid : Parent_ProcessId= $ppid : Average_Running_Time= $art" >> tmp.txt
done

cat tmp.txt | sort -n -k5 > ans4
rm tmp.txt
