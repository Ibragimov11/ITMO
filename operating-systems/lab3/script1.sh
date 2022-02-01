#!/bin/bash

rm -r ~/test
d=$(date + "%F_%R")
mkdir ~/test && echo "catalog test was created successfully" > ~/report && touch ~/test$d
ping -c 1 "www.net_nikogo.ru" || echo $(date + "%F_%R") "ping error" >> ~/report