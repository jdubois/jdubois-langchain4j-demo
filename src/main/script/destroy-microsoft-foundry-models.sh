#!/usr/bin/env bash

# Execute this script to destroy the Microsoft Foundry models and associated services.

echo "Reading environment variables..."

SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
source $SCRIPTPATH/../../../.env

echo "----------------------------------"
echo "OPENAI_BASE_URL=$OPENAI_BASE_URL"
echo "AZURE_RESOURCE_GROUP=$AZURE_RESOURCE_GROUP"
echo "AZURE_LOCATION=$AZURE_LOCATION"
echo "AZURE_AI_SERVICE=$AZURE_AI_SERVICE"

# stop if the environment variables are not set
if [ -z "$OPENAI_BASE_URL" ]; then
  echo "The OPENAI_BASE_URL environment variable is not set."
  exit 1
fi

echo "Deleting the resource group..."
echo "------------------------------"
az group delete \
  --name "$AZURE_RESOURCE_GROUP" \
  --yes

echo "Purging the models..."
echo "------------------------------"
az cognitiveservices account purge \
  --location "$AZURE_LOCATION" \
  --name "$AI_SERVICE" \
  --resource-group "$AZURE_RESOURCE_GROUP"

echo "Done!"
