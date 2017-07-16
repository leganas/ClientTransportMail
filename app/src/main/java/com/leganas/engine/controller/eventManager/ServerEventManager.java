package com.leganas.engine.controller.eventManager;

import com.leganas.engine.interfaces.Manager;
import com.leganas.engine.interfaces.ProgController;
import com.leganas.engine.network.packeges.clientTOserver.ClientMessage;

/**
 * Занимается обработкой и исполнением
 * всех поступивших в него сообщений типа ClientMessage
 */
public class ServerEventManager extends Manager<ClientMessage> {

    public ServerEventManager(ProgController<?> progController) {
        super(progController);
    }
}
