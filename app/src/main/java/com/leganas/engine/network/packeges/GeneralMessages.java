package com.leganas.engine.network.packeges;

/**Технические сообщения клиент/сервер*/
public class GeneralMessages{

	static public class RegisterName{
		public String name;

	}

	static public class UpdateNames{
		public String[] names;

	}

	static public class ChatMessage{
		public String text;
		
		public ChatMessage(String text) {
			super();
			this.text = text;
		}

		public ChatMessage() {
			super();
		}

		
	}

}
