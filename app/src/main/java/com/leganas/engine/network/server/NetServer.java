
package com.leganas.engine.network.server;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.leganas.engine.utils.Logs;
import com.leganas.engine.Status;
import com.leganas.engine.controller.ServerController;
import com.leganas.engine.network.Network;
import com.leganas.engine.network.packeges.clientTOserver.ClientMessage;
import com.leganas.engine.network.packeges.GeneralMessages;

public class NetServer extends AbstractServer{
	Server server;
	public boolean status=false;


	public NetServer (ServerController serverController) {
		super(serverController);
		server = new Server(256000,256000) {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				Status.statusServer = Status.StatusServer.run;
				return new SConnection();
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);
		
		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				// We know all connections for this server are actually ChatConnections.
				SConnection connection = (SConnection)c;

				if (object instanceof GeneralMessages.RegisterName) {
					// Ignore the object if a client has already registered a name. This is
					// impossible with our client, but a hacker could send messages at any time.
					if (connection.name != null) return;
					// Ignore the object if the name is invalid.
					String name = ((GeneralMessages.RegisterName)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					// Store the name on the connection.
					connection.name = name;
					// Send a "connected" message to everyone except the new client.
					GeneralMessages.ChatMessage chatMessage = new GeneralMessages.ChatMessage();
					chatMessage.text = "Server : chatMessage - "+name + " connected.";
					server.sendToAllExceptTCP(connection.getID(), chatMessage);
					// Send everyone a new list of connection names.
					updateNames();
					return;
				}

				if (object instanceof GeneralMessages.ChatMessage) {
					// Ignore the object if a client tries to chat before registering a name.
					if (connection.name == null) return;
					GeneralMessages.ChatMessage chatMessage = (GeneralMessages.ChatMessage)object;
					// Ignore the object if the chat message is invalid.
					String message = chatMessage.text;
					if (message == null) return;
					message = message.trim();
					if (message.length() == 0) return;
					// Prepend the connection's name and send to everyone.
					chatMessage.text = connection.name + ": " + message;
					netServerListener.netServerMessage(connection.getID(),message);
					server.sendToAllTCP(chatMessage);
					return;
				}
				netServerListener.netServerMessage(connection.getID(),object);

			}

			public void disconnected (Connection c) {
				SConnection connection = (SConnection)c;
				if (connection.name != null) {
					// Announce to everyone that someone (with a registered name) has left.
					GeneralMessages.ChatMessage chatMessage = new GeneralMessages.ChatMessage();
					chatMessage.text = connection.name + " disconnected.";
					server.sendToAllTCP(chatMessage);
					updateNames();
					ClientMessage.DisconectPlayer msg = new ClientMessage.DisconectPlayer();
					netServerListener.netServerMessage(c.getID(), msg);
				}
			}
		});
		try {
			server.bind(Network.portTCP, Network.portUDP);
			server.start();
		} catch (Exception e) {
			Logs.out("ERROR Server already running");
			System.exit(0);
		}
	}
	
	public int getCollClient(){
		return server.getConnections().length;
	}

	@Override
	public void dispose(){
		server.stop();
//		Gdx.app.log("LGame", "Server stop");
    }
	void updateNames () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList<String> names = new ArrayList<String>(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			SConnection connection = (SConnection)connections[i];
			names.add(connection.name +" id= "+i);
		}
		// Send the names to everyone.
		GeneralMessages.UpdateNames updateNames = new GeneralMessages.UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAllTCP(updateNames);
	}
	
	public void sendToAllUDP(Object message){
		server.sendToAllUDP(message);
	}
	
	public void sendToAllTCP(Object message){
		server.sendToAllTCP(message);
	} 
	
	public void sendtoTCP(int connectionID, Object message){
		server.sendToTCP(connectionID, message);
	} 
	
	public void sendtoUDP(int connectionID, Object message){
		server.sendToUDP(connectionID, message);
	}

	// This holds per connection state.
	static class SConnection extends Connection {
		public String name;
	}
}
