
package com.leganas.engine.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.leganas.engine.NetClientCard;
import com.leganas.engine.UserCard;
import com.leganas.engine.WorkData;
import com.leganas.engine.network.packeges.GeneralMessages.ChatMessage;
import com.leganas.engine.network.packeges.GeneralMessages.RegisterName;
import com.leganas.engine.network.packeges.GeneralMessages.UpdateNames;
import com.leganas.engine.network.packeges.clientTOserver.ClientMessage;
import com.leganas.engine.network.packeges.serverTOclient.ServerMessage;

import java.util.ArrayList;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int portTCP = 8900;
	static public final int portUDP = 8902;

	
	
	public static int getPorttcp() {
		return portTCP;
	}

	public static int getPortudp() {
		return portUDP;
	}

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(float[].class);
		kryo.register(int[].class);
		kryo.register(Object.class);
		kryo.register(String[].class);
		kryo.register(Object[].class);
		kryo.register(ArrayList.class);
		kryo.register(WorkData.class);
		kryo.register(NetClientCard.class);
		kryo.register(NetClientCard.Possition.class);
		kryo.register(UserCard.Permission.class);


		kryo.register(RegisterName.class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		
		kryo.register(ClientMessage.class);
		kryo.register(ClientMessage.RequestServerInfo.class);
		kryo.register(ClientMessage.DisconectPlayer.class);
		kryo.register(ClientMessage.RequestAutorization.class);
		kryo.register(ClientMessage.RequestWorkData.class);

		kryo.register(ServerMessage.class);
		kryo.register(ServerMessage.ReturnServerInfo.class);
		kryo.register(ServerMessage.ReturnAutorizationStatus.class);
		kryo.register(ServerMessage.ReturnWorkData.class);
	}

}
