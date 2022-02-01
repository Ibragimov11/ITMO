#!/bin/bash

mail="[a-zA-Z][a-zA-Z0-9.-]+@[a-zA-Z0-9]+(\\.[a-zA-Z]+)+"
grep -E -r -o -h "$mail" /etc/* | awk '{printf("$s, ", $1)}' > emails.lst