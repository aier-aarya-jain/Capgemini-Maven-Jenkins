FROM eclipse-temurin:17-jre

# Install system dependencies for X11, VNC, noVNC, and Openbox
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y \
    xvfb \
    x11vnc \
    novnc \
    websockify \
    openbox \
    sed && \
    rm -rf /var/lib/apt/lists/*

# Symlink vnc.html to index.html so noVNC loads automatically at root URL
RUN ln -s /usr/share/novnc/vnc.html /usr/share/novnc/index.html

WORKDIR /app

# Copy the pre-compiled JAR (built via host Maven) and entrypoint script
COPY target/Maven1-1.0-SNAPSHOT.jar /app/snake-game.jar
COPY entrypoint.sh /app/entrypoint.sh

# Sanitize line endings (Windows CRLF -> Linux LF) and make executable
RUN sed -i 's/\r$//' /app/entrypoint.sh && chmod +x /app/entrypoint.sh

# Expose port 8080 for noVNC web interface
EXPOSE 8080

# Set entrypoint
ENTRYPOINT ["/bin/bash", "/app/entrypoint.sh"]