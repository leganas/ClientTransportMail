package com.leganas.engine.controller;

import com.leganas.engine.Assets;
import com.leganas.engine.Status;
import com.leganas.engine.WorkData;
import com.leganas.engine.interfaces.Manager;
import com.leganas.engine.interfaces.ProgController;
import com.leganas.engine.network.packeges.clientTOserver.ClientMessage;
import com.leganas.engine.network.packeges.serverTOclient.ServerMessage;
import com.leganas.engine.network.server.AbstractServer;
import com.leganas.engine.network.server.NetServer;
import com.leganas.engine.controller.eventManager.ServerEventManager;
import com.leganas.engine.utils.Logs;

import java.util.ArrayList;

/**
 * Серверный контроллер
 * Назначение :
 * - создаёт и запускает NetServer
 * - управляет всей внутренней работой сервера
 * - Обновляет WorkData , и отсылает по запросу или по таймеру копию клиенту
 */

public class ServerController extends ProgController<WorkData> implements AbstractServer.NetServerListener, Manager.ManagerListener {
    /**Интерфейс обратной связи c Окном графического интерфейса, через назначенного слушателя*/
    public interface GUIListener {
        public void GUIMessage(Object msg);
    }


    NetServer server;
    ServerEventManager serverEventManager;
    ArrayList<ServerMessage> sendQuery;
    GUIListener  guiListener;

    public GUIListener getGuiListener() {
        return guiListener;
    }

    public void setGuiListener(GUIListener guiListener) {
        this.guiListener = guiListener;
    }

    public ServerController(String name) {
        super(name);
        sendQuery = new ArrayList<>();
    }


    @Override
    public void init() {
        Status.statusServer = Status.StatusServer.init;
        server =  new NetServer(this);
        server.setGameServerListener(this);
        serverEventManager = new ServerEventManager(this);
        serverEventManager.setListener(this);
        Assets.workData = new WorkData();
        TestCardGenerate();
    }

    private void TestCardGenerate(){
//        NetClientCard card = new NetClientCard();
//        card.setName("Андрей");
//        Assets.workData.getOnlineDriver().add(card);
    }


    public void addServerMessageToQuery(ServerMessage msg){
        synchronized (sendQuery) {
            sendQuery.add(msg);
        }
    }

    void sendAllQueryToClient(){
        synchronized (sendQuery) {
            if (sendQuery.size() > 0) {
                for (int i = 0; i < sendQuery.size(); i++) {
                    if (sendQuery.get(i).getId() == -1) {
                        server.sendToAllTCP(sendQuery.get(i));
                    } else {
                      server.sendtoTCP(sendQuery.get(i).getId(), sendQuery.get(i));}
                }
                sendQuery.clear();
            }
        }
    }


    @Override
    public void update() {
        sendAllQueryToClient();
        if (Status.statusServer == Status.StatusServer.dispose) dispose();
        if (Status.statusServer == Status.StatusServer.turn) System.exit(0);
        // Выполняем команда в отдельном потоке сервер контроллера
        if (serverEventManager != null){
            serverEventManager.process();
        }
    }

    /**Тут мы получаем сообщения из сети от клиентов*/
    @Override
    public void netServerMessage(int connectionID, Object event) {
        if (event instanceof ClientMessage) {
            Logs.out("Server : Принято сообщение от клиента - " +event);

            // если получена команда от клиента то ставим на обработку
            // пока хуй знает куда девать connectionID
            ClientMessage ev = (ClientMessage) event;
            // Устанавливаем ID для полученого события
            // (как индификатор пользователя который нам это прислал)
            // потом при помощи него в методе ListenerMessage мы отправим именно
            // этому клиенту ответное сообщение назад
            ev.setId(connectionID);
            serverEventManager.addEventToQueue(ev);
        }
    }

    /**Тут мы получаем обратные вызовы от нашего ServerEventManager*/
    @Override
    public void ListenerMessage(Object msg) {
        if (guiListener != null) guiListener.GUIMessage(msg);
        if (msg instanceof ServerMessage) {
            Logs.out("Server : Сформирован ответ для клиента, отправляем - " +msg);
            // Получили от евент менеджера сообщение для клиента значит отправляем клиенту
            addServerMessageToQuery((ServerMessage) msg);
//            server.sendtoTCP(m.getId(),msg);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        server.dispose();
    }

}
