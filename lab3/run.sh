#!/bin/bash

mvn clean package
cd spark-3.5.1-bin-hadoop3
# Chequear si feeds.txt existe y eliminarlo si es así
if [ -f feeds.txt ]; then
    rm feeds.txt
fi
./bin/spark-submit --master local[2] ../target/EntitiesClassifier-0.1.jar "$@" 2> /dev/null
cd ..