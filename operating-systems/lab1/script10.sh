#!/bin/bash

man bash | grep -o "[[:alpha:]]\{4,\}" | sort | uniq -c | sort -r -n | head -3