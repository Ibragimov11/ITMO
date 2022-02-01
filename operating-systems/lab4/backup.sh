#!/bin/bash

cd ~
dateCur=$(date +"%Y-%m-%d")
datePrev=$(ls | grep "^Backup-" | sort -n | tail -1 | cut -d "-" -f 2,3,4)
echo $datePrev

if [[ $(echo $(date -d $dateCur +"%s") $(date -d $datePrev +"%s") | awk '{print ($1 - $2) / 60 / 60 / 24}') -ge 7 ]]
then
  mkdir "Backup-$dateCur"
  listFilesInSource=$(ls ~/source)

  for fileInList in $listFilesInSource;
    do
      cp -r $fileInList ~/Backup-$dateCur
    done

  echo "Files were backed up in the new directory Backup-$dateCur"
  echo "Files were backed up in the new directory Backup-$dateCur" >> backup-report
  echo "Files:" >> backup-report
  echo "$listFilesInSource" >> backup-report
else
  listFilesInSource=$(ls ~/source)
  echo "Updated Backup-$dateCur directory"
  echo "Updated Backup-$dateCur directory" >> backup-report
  echo "Files:" >> backup-report
  echo "$listFilesInSource" >> backup-report

  cd ~/Backup-$datePrev

  for fileInList in $listFilesInSource;
    do
      if [[ -f $fileInList ]];
      then
        sizeSourceFile=$(ls -l ~/source/$fileInList | awk '{print $5}')
        sizeBackupFile=$(ls -l $fileInList | awk '{print $5}')

        filesAreDifferent=$(diff -q $fileInList ~/source/$fileInList)

        if [[ $filesAreDifferent ]];
        then
          mv $fileInList $fileInList-$dateCur
          cp -r ~/source/$fileInList ~/Backup-$datePrev
          echo "Renamed $fileInList to $fileInList-$dateCur" >> ~/backup-report
        fi
      else
        cp -r ~/source/$fileInList ~/Backup-$dateCur
      fi
    done
fi
