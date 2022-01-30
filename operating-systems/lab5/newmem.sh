#!/bin/bash

array=()
cnt=0
N=$1
size=0

while [[ $size -lt $N ]]
do
  array+=(1 2 3 4 5 6 7 8 9 10)
  cnt=$(($cnt+1))
  size=$((size+10))
done