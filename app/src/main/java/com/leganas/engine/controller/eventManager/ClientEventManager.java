package com.leganas.engine.controller.eventManager;

import com.leganas.engine.interfaces.Manager;
import com.leganas.engine.interfaces.ProgController;
import com.leganas.engine.network.packeges.serverTOclient.ServerMessage;

/**
 * Занимается обработкой и исполнением
 * всех поступивших в него сообщений ServerMessage
 */
public class ClientEventManager extends Manager<ServerMessage> {

    public ClientEventManager(ProgController<?> progController) {
        super(progController);
    }
}
