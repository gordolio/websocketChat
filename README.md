# Websocket Chat
###### Experiments with websockets

This is a chat client made using websockets. The backend is using springboot with spring-messaging. The frontend is using sockjs + stomp.

#### Features

1. Chat
   * Socket based chat / instant communication
   * Room list
   * Join/Leave notify events
   * Typing events
2. Planning poker voting
3. Nothing is stored on the server. All communication is handled "in-memory" using plain old java collections.

Battleship functionality is currently being worked on.

#### Running

./gradlew run     # *nix
gradlew.bat run   # windows

navigate your webbrowser to http://localhost:8081/
