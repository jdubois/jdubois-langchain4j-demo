#!/bin/bash
sleep 5
apt-get update -yq && apt-get install -yqq curl
curl -X PUT 'http://localhost:6333/collections/kbindex' \
     -H 'Content-Type: application/json' \
     --data-raw '{
       "vectors": {
         "size": 384,
         "distance": "Cosine"
       }
     }'
