#!/usr/bin/env bash

# Execute this script to destroy the Azure OpenAI models and associated services.

echo "Reading environment variables..."

SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
source $SCRIPTPATH/../../../.env

echo "----------------------------------"
echo "AZURE_OPENAI_ENDPOINT=$AZURE_OPENAI_ENDPOINT"
echo "RESOURCE_GROUP=$RESOURCE_GROUP"
echo "LOCATION=$LOCATION"
echo "AI_SERVICE=$AI_SERVICE"

// stop if the environment variables are not set
if [ -z "$AZURE_OPENAI_ENDPOINT" ]; then
  echo "The AZURE_OPENAI_ENDPOINT environment variable is not set."
  exit 1
fi

echo "Deleting the resource group..."
echo "------------------------------"
az group delete \
  --name "$RESOURCE_GROUP" \
  --yes

echo "Purging the models..."
echo "------------------------------"
az cognitiveservices account purge \
  --location "$LOCATION" \
  --name "$AI_SERVICE" \
  --resource-group "$RESOURCE_GROUP"

echo "Done!"
