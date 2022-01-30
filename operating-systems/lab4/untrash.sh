#!/bin/bash

file=$1
log=~/.trash.log
trash=$(cat $log)

for string in $trash
do
  fullNameFile="$(echo $string | cut -d "=" -f 1)"
  shortNameFile=$(echo $fullNameFile | awk -F/ '{print $NF}')
  if [[ $1 == "$shortNameFile" ]]
  then
    echo "Do you want to restore" $shortNameFile "? [y/n]"
    read ans

    if [[ $ans == "y" ]]
    then
      dirPath=$(echo "$fullNameFile" | awk -F/ 'OFS="/"{$NF="" ; print $0}')
      recovery=~/.trash/"$(echo "$string" | cut -d "=" -f 2)"

      if ! [[ -d $dirPath ]]
      then
        echo "Directory was deleted/ Redirection in /home"
        dirPath="home/user"
      fi

      newName="$dirPath""$shortNameFile"
      while true
      do
        if [[ -f "$newName" ]]
        then
          echo "fileName already exist. New name: "
          read shortNameFile
          newName="$dirPath""$shortNameFile"
        else
          break
        fi
      done

      if [[ -f "$recovery" ]]
      then
        ln "$recovery" "$newName"
        rm "$recovery"
      fi
    fi
  fi
done