#!/bin/bash

operation="+"
res=1

(tail -f file5) | while true
do
  read string
  case $string in
    "+")
      operation="+"
      ;;
    "*")
      operation="\*"
      ;;
    "-")
      operation="-"
      ;;
    [0-9]*)
      if [[ $operation == "+" ]]
      then
        let res=$res+$string
      else
        if [[ $operation == "-" ]]
          then
            let res=$res-$string
          else
            let res=$res*$string
        fi
      fi
      echo $res
      ;;
    "QUIT")
      echo "Executed successfully"
      kill $(cat pid5)
      exit
      ;;
    *)
      echo "Undefined behavior" > stderr5
      kill $(cat pid5)
      exit
      ;;
done
