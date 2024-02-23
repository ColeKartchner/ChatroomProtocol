# ChatroomProtocol
This is a program designed to demonstrate how to design and implement your own protocol, working with the other members of our class we each implemented the same protocol to make a chatroom that we could all connect to, the details are listed here:

Chatroom Protocol
TCP Port: 5040
Codes (from server)
1 = username is taken
2 = reserved character used in username
3 = length of username is either too long or too short
4 = success making a username
5 = Message is too long or too short
6 = Reserved character used in message	
7 = private message has been received
8 = public message has been received
9 = Message recipient not found

Joining
4 codes/outcomes (codes are from server):
Username taken fail (Server closes connection)
Reserved character used (Server closes connection)
Length of username is invalid (Server closes connection)
Success
Usernames are case sensitive
The server updates the rest of the chatroom with a broadcast message that says a new user has joined with the name of that user.
	Format: “<Username> joined the chatroom/server.”
Server will receive this message from the client to establish a new user - user<username>\n
Reserved username: server
Protected characters: <>,		(left angle bracket, right angle bracket, comma)
Length: 1 - 20 characters

Chatting
Chatting codes/outcomes (sent from server to client):
Message length invalid
Reserved character used
Private message success
Public msg success
Recipient username does not exist (for private messages)

How to specify broadcast or private messages:
For broadcast -  broadcast<sender username,time,message>\n
	Server will send an 8 code on successful broadcast back to user who is broadcasting
	Server will send the broadcast message to all users connected to the chatroom, including the sender
For private - private<sender username,recipient username,time,message>\n
	Server will send a 7 code on successful private message
	If the username that the user sends to does not exist, Server sends a 9 code back to the user who’s trying to send it and doesn’t send the private message to anyone.
	Server will send the Private message to the recipient and the sender
Example:
	Server gets - private<nathan,Landon, 7:14,message>
	Nathan and Landon clients both get: private<nathan, Landon, 11-9, message>
	Similar logic for broadcasts

For messages from the server - we just use a broadcast or private for every server message. When a user joins or leaves it broadcasts that they joined or left.
Server will get this message from the client for sending a list of users - ls<requesting user>\n
Server will send this back to the client (sigh) - userlist<list of users(comma delimited)>\n
Time is 24 hour time
Reserved characters: <>
Message length limit: 1-1000
Leaving
For telling the server that the client wants to leave the chatroom - exit<username>\n

Server will close the connection to this client.
Server closes connection
Server will send a broadcast message saying that the user has left the chatroom.
	Format: “<Username> left the chatroom/server.”

Extra Notes
In our clients, we should handle keyboard interrupt (ctrl + c) so that the client sends an exit request to the server
Server userlist is updated
I wouldn’t recommend making the server a user like we talked about, it created a huge headache. Thankfully we didn’t include this in the protocol.

Connection opens, username is the only option
In order to continue, we need an if statement or some kind of check that the client is past the username stage. Somehow see if the username is established.

