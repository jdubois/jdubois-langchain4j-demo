#!/bin/bash

# Start Ollama in the background.
/bin/ollama serve &
# Record Process ID.
pid=$!

# Pause for Ollama to start.
sleep 5

echo "🔴 Retrieve Phi-3 model..."
ollama pull phi3
echo "🟢 Done!"

# Wait for Ollama process to finish.
wait $pid
