#!/usr/bin/env bash
echo "Checking for database status ..."
./wait-for-it.sh postgres:14.2:5432 -t 15 &
java -jar stock-0.0.1-SNAPSHOT.jar