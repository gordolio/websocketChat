# Websocket Chat
###### Experiments with websockets

This is a chat client made using websockets. The backend is using springboot with spring-messaging. The frontend is using sockjs + stomp.

This supports the following features:

1. Chat
   * Socket based chat / instant communication
   * Room list
   * Join/Leave notify events
   * Typing events
2. Planning poker voting

Battleship functionality is currently being worked on.

Nothing is stored on the server. All communication is handled "in-memory" using plain old java collections.

