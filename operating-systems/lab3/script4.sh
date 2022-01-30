#!/bin/bash

bash calc1.sh &
bash calc2.sh &
bash calc3.sh &

kill $(cat pid3)
cpulimimt -p $(cat pid1) -l 1- -b
