# Smart Path Relay
A simple service that connects to the Gallery Service via websocket and enrols the traveller into Smart Path.

Accept the ADD traveller message from the Gallery Service and call the /enrol API on the Smart Path Hub server.

# Installation
First time only, download the source project from GitHub.
```
git clone ...
```

On subsequent updates, refresh the project from GitHub.
```
git pull
```

# Build and Run
Perform a clean install and run the application
```
./mvnw clean install
./mvnw spring-boot:run
```

# Local Browser Access
Access the webapp from a browser via http://localhost:8004


