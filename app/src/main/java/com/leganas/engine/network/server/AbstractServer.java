package com.leganas.engine.network.server;

import com.leganas.engine.interfaces.Disposable;
import com.leganas.engine.controller.ServerController;
import com.leganas.engine.interfaces.Net;

public abstract class AbstractServer extends Net<ServerController> implements Disposable {
	

	public AbstractServer(ServerController serverController) {
		super(serverController);
	}

	public interface NetServerListener {
		public void netServerMessage(int connectionID,Object event);
	}
	NetServerListener netServerListener;

	public void setGameServerListener(NetServerListener gameServerListener) {
		this.netServerListener = gameServerListener;
	}

	public int getCollClient(){
		return 0;
	}

	public abstract void sendToAllUDP(Object message);
	
	public abstract void sendToAllTCP(Object message); 
	
	public abstract void sendtoTCP(int connectionID, Object message);
	
	public abstract void sendtoUDP(int connectionID, Object message);

}
