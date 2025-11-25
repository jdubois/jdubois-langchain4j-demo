#!/bin/bash

# Start Ollama in the background.
/bin/ollama serve &
# Record Process ID.
pid=$!

# Pause for Ollama to start.
sleep 5

echo "🔴 Retrieve Ollama models..."
ollama pull nomic-embed-text
ollama pull llama3.2:1b
echo "🟢 Done!"

# Wait for Ollama process to finish.
wait $pid
