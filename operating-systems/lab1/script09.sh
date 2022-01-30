#!/bin/bash

count=0

for file in /var/log/*.log;
do
  let count+='cat $file | wc -l'
done

echo $count