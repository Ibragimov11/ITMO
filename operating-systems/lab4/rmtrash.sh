#!/bin/bash

file=$1
trash=~/.trash

if ! [[ -d ~/.trash ]];
then
  mkdir $trash
fi

if [[ -f $file ]];
then
  link=$(date + "%F_%T")
  ln $file "$trash/$link"
  echo "$(realpath $file)=$link" >> ~/.trash.log
  rm $file
else
  echo "It's not a file"
  exit 1
fi
