#!/bin/bash

cd ~

if ! [[ -d restore ]];
then
  mkdir restore
fi

cd ~/$(ls | grep '^Backup-' | sort -n | tail -1)

for fileInList in $(ls);
do
  if ! [[ $fileInList =~ -[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]];
  then
    cp -R $fileInList ~/restore
  fi
done
