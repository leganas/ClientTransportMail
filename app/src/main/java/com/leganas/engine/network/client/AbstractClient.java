package com.leganas.engine.network.client;


import com.leganas.engine.interfaces.Disposable;
import com.leganas.engine.controller.ClientController;
import com.leganas.engine.interfaces.Net;

public abstract class AbstractClient extends Net<ClientController> implements Disposable {
	String name;

	public AbstractClient(ClientController clientGameController) {
		super(clientGameController);
	}

	public interface NetClientListener {
		public void netClientMessage(Object event);
	}

	NetClientListener netClientListener;


	public void setNetClientListener(NetClientListener netClientListener) {
		this.netClientListener = netClientListener;
	}

	public void sendMSGtoListener(Object event) {
		netClientListener.netClientMessage(event);
	}

	public abstract void sendtoTCP(Object message);

	public void sendMessage(Object message) {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
