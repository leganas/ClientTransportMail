
package com.leganas.engine.network.client;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.leganas.engine.Status;
import com.leganas.engine.controller.ClientController;
import com.leganas.engine.interfaces.Logs;
import com.leganas.engine.network.Network;
import com.leganas.engine.network.packeges.GeneralMessages;

import java.io.IOException;

public class NetClient extends AbstractClient{
	Client client;

	public NetClient(ClientController clientGameController) {
		super(clientGameController);
	}

	public NetClient (final ClientController clientGameController, String myhost) {
		super(clientGameController);
		client = new Client(256000,256000);
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
				GeneralMessages.RegisterName registerName = new GeneralMessages.RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
				ClientInfo.UserID = client.getID();
				Status.clientStatus = Status.ClientStatus.online;
			}

			public void received (Connection connection, Object object) {
				netClientListener.netClientMessage(object);
			}

			public void disconnected (Connection connection) {
				Logs.out("netClient server down");
			}
		});

		String input = myhost;
		final String host = input.trim();
		
		input = "ClientName";
		name = input.trim();
		connect(host);
	}

	public void connect(final String host){
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(50000, host, Network.portTCP, Network.portUDP);
				} catch (IOException ex) {
					// Временно просто отправляем ClientController, ру строку о том что произошёл сбой подключения
					netClientListener.netClientMessage("connect fail");
//					System.exit(0);
				}
			}
		}.start();

	}
	
	
	@Override
	public void sendtoTCP(Object message) {
		sendMessage(message);
	}

	public  void sendMessage(Object message){
	try {
		client.sendTCP(message);
	} catch (Exception e) {
	}
	}

	@Override
	public void dispose() {
		client.stop();
//		Gdx.app.log("LGame", "netClient disconect from the server and dispose");
	}
}
