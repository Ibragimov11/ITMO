#!/bin/bash

k=$1
N=1000000

for (( i = 0; i < $k; i++ ));
do
  bash newmem.bash $N &
  sleep 1
done
