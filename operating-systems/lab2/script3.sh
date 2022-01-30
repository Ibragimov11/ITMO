#!/bin/bash

ps aux | awk '{print $2 " " $9}' | grep -E '[0-9]' | sort -k2 | tail -1 | awk '{print $1}' > ans3
