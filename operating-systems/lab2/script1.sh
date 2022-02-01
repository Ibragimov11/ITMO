#!/bin/bash

ps -U root | wc -l > ans1
ps -U root | awk '{print $1 ":" $4}' >> ans1