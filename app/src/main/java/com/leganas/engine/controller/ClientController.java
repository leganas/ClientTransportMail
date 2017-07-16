package com.leganas.engine.controller;

import com.leganas.engine.Assets;
import com.leganas.engine.Setting;
import com.leganas.engine.Status;
import com.leganas.engine.WorkData;
import com.leganas.engine.interfaces.Logs;
import com.leganas.engine.interfaces.Manager;
import com.leganas.engine.interfaces.ProgController;
import com.leganas.engine.network.client.AbstractClient;
import com.leganas.engine.network.client.NetClient;
import com.leganas.engine.network.packeges.clientTOserver.ClientMessage;
import com.leganas.engine.network.packeges.serverTOclient.ServerMessage;
import com.leganas.engine.controller.eventManager.ClientEventManager;

import java.util.ArrayList;

/**
 * Created by AndreyLS on 08.02.2017.
 */
public class ClientController extends ProgController<WorkData> implements AbstractClient.NetClientListener, Manager.ManagerListener {
    /**Интерфейс обратной связи c Окном графического интерфейса, через назначенного слушателя*/
    public interface GUIListener {
        public void GUIMessage(Object msg);
    }

    public NetClient client;
    ClientEventManager clientEventManager;
    ArrayList<ClientMessage> sendQuery;
    GUIListener guiListener;

    public GUIListener getGuiListener() {
        return guiListener;
    }

    public void setGuiListener(GUIListener guiListener) {
        this.guiListener = guiListener;
    }

    public ClientController(String name) {
        super(name);
        sendQuery = new ArrayList<>();
    }

    @Override
    public void init() {
        client = new NetClient(this, Setting.IPAdressFromClient);
        client.setNetClientListener(this);
        clientEventManager = new ClientEventManager(this);
        clientEventManager.setListener(this);
        if (Assets.workData == null) Assets.workData = new WorkData();
    }

    public void addClientMessageToQuery(ClientMessage msg){
        synchronized (sendQuery) {
            sendQuery.add(msg);
        }
    }

    void sendAllQueryToServer(){
        synchronized (sendQuery) {
            if (sendQuery.size() > 0) {
                for (int i = 0; i < sendQuery.size(); i++) {
                    client.sendtoTCP(sendQuery.get(i));
                }
                sendQuery.clear();
            }
        }
    }

    @Override
    public void update() {
        if (Status.clientStatus != Status.ClientStatus.offline) {
            sendAllQueryToServer();
        }
        if (clientEventManager != null) clientEventManager.process();
    }

    @Override
    public void dispose() {
        super.dispose();
        client.dispose();
    }

    @Override
    public void netClientMessage(Object event) {
        if (event instanceof  String) {
            Logs.out((String) event);
        }
        if (event instanceof ServerMessage) {
            // если получена команда от сервера то ставим на обработку
            ServerMessage ev = (ServerMessage) event;
            clientEventManager.addEventToQueue(ev);
        }
    }

    @Override
    public void ListenerMessage(Object msg) {
        // Если в результате выполения команды/сообщения от сервака сформирован ответ то отправим его нахуй
        // Нашуму GUI и за одно назад серверу что бы он сука знал что мы получили его сообщения и действуем
        // хотя он сука и так знает что мы получили но пусть блять будет уверен в этом :)
        if (guiListener != null) guiListener.GUIMessage(msg);
        if (msg instanceof ClientMessage) {
            addClientMessageToQuery((ClientMessage) msg);
            // client.sendtoTCP(msg);
        }
    }
}
