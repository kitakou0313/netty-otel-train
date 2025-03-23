#!/bin/bash

# 送信回数
COUNT=5
# リクエスト先URL
URL="http://localhost:8080"

# リクエストを繰り返し送信
for ((i=1; i<=COUNT; i++))
do
  echo "Sending request $i..."
  curl -X GET "$URL"
  echo ""
done

echo
echo "All requests sent."
