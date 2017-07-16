package com.leganas.engine.network.packeges.clientTOserver;


import com.leganas.engine.Assets;
import com.leganas.engine.NetClientCard;
import com.leganas.engine.Setting;
import com.leganas.engine.UserCard;
import com.leganas.engine.interfaces.Logs;
import com.leganas.engine.controller.ServerController;
import com.leganas.engine.interfaces.Event;
import com.leganas.engine.interfaces.Message;
import com.leganas.engine.network.packeges.serverTOclient.ServerMessage;

public abstract  class ClientMessage extends Message<ServerController> {

	public static class RequestServerInfo extends ClientMessage{
		public RequestServerInfo() {}

		@Override
		public Event ResponseMessage(ServerController controller, int id) {
			Logs.out("Получен запрос серверной информации, формируем ответ");
			ServerMessage.ReturnServerInfo msg = new ServerMessage.ReturnServerInfo();
			msg.setName(Setting.ServerName);
			msg.setId(id); // какому клиенту послать
			return msg;
		}
	}

	public static class RequestAutorization extends ClientMessage {
		public String login;
		public String password;

		public RequestAutorization() {}

		@Override
		public Event ResponseMessage(ServerController controller, int id) {
			ServerMessage.ReturnAutorizationStatus status = new ServerMessage.ReturnAutorizationStatus();
			status.flag = false;
			for (int i = 0; i< Assets.users.userList.size(); i++){
				if (Assets.users.userList.get(i).getLogin().equals(login)){
					if (Assets.users.userList.get(i).getPassword().equals(password)) {
						status.flag = true;
						status.permission = Assets.users.userList.get(i).getPermission();

						// добавляем нового онлайн юзверя
						NetClientCard card = new NetClientCard();
						card.setName(Assets.users.userList.get(i).getName());
						card.setPermission(Assets.users.userList.get(i).getPermission());
						card.setNet_id(id);

						addNewUser(card);
						sendUpdataAllWorkData(controller);
					}
				}
			}
			if (login.equals("MobileUser")) {
				status.flag = true;
				status.permission = UserCard.Permission.MobileUser;
				// добавляем нового онлайн юзверя
				NetClientCard card = new NetClientCard();
				card.setName(login + id);
				card.setPermission(UserCard.Permission.MobileUser);
				card.setNet_id(id);

				addNewUser(card);
				sendUpdataAllWorkData(controller);
			}
			status.setId(id); // какому клиенту послать
			return status;
		}

	}

	public static void addNewUser(NetClientCard card){
		if (card.getPermission() == UserCard.Permission.ServerAdmin ||
				card.getPermission() == UserCard.Permission.MobileDriver ||
				card.getPermission() == UserCard.Permission.MobileServerAdmin)
			    Assets.workData.getOnlineDriver().add(card);
		if (card.getPermission() == UserCard.Permission.MobileUser){
			Assets.workData.getOnlineUsers().add(card);
		}
	}

	private static void sendUpdataAllWorkData(ServerController controller) {
		ServerMessage.ReturnWorkData msg = new ServerMessage.ReturnWorkData();
		synchronized (Assets.workData) {
			msg.setWorkData(Assets.workData);
		}
		msg.setId(-1);
		controller.addServerMessageToQuery(msg);
	}
	public static class RequestWorkData extends ClientMessage {

		public RequestWorkData() {
		}

		@Override
		public Event ResponseMessage(ServerController controller, int id) {
			Logs.out("Получен запрос пользовательских данных");
			ServerMessage.ReturnWorkData msg = new ServerMessage.ReturnWorkData();
			synchronized (Assets.workData) {
				msg.setWorkData(Assets.workData);
			}
			msg.setId(id);
			return msg;
		}
	}

	public static class DisconectPlayer extends ClientMessage{
		
		public DisconectPlayer() {
			super();
		}

		@Override
		public Event ResponseMessage(ServerController controller, int id) {
			for (int i=0;i < Assets.workData.getOnlineDriver().size();i++){
				if (Assets.workData.getOnlineDriver().get(i).getNet_id() == id) {
					Assets.workData.getOnlineDriver().remove(Assets.workData.getOnlineDriver().get(i));
				}
			}
			for (int i=0;i < Assets.workData.getOnlineUsers().size();i++){
				if (Assets.workData.getOnlineUsers().get(i).getNet_id() == id) {
					Assets.workData.getOnlineUsers().remove(Assets.workData.getOnlineUsers().get(i));
				}
			}
			sendUpdataAllWorkData (controller);
			return null;
		}
	}
}