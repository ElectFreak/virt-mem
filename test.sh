#!/bin/bash

for testNumber in $(seq 10); do
  diff <(java -jar build/libs/virtmem-1.0-SNAPSHOT.jar < <(echo "test/test$testNumber")) "test/result$testNumber"
done