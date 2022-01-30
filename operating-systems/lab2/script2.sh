#!/bin/bash

files=$(ls /sbin)

for file in $(echo "files")
do
  ps awk | grep $file | awk '{print $2}' > ans2
done