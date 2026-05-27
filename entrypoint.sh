#!/bin/bash

# Start Xvfb virtual frame buffer
Xvfb :99 -screen 0 800x600x16 &
export DISPLAY=:99

# Wait for Xvfb to start
sleep 1

# Start Openbox Window Manager
openbox &

# Start VNC Server (without password, listening locally)
x11vnc -display :99 -forever -nopw -shared -listen 127.0.0.1 &

# Start noVNC proxy to bridge WebSocket on port 8080 to VNC on port 5900
websockify --web=/usr/share/novnc 8080 127.0.0.1:5900 &

# Start the Crazy Snake Game Swing app
echo "Starting Crazy Snake Game..."
exec java -jar /app/snake-game.jar
