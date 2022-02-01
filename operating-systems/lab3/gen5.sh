#!/bin/bash

echo $$ > pid5

rm -r file5

while true
do
  read string
  echo $string >> file5
done