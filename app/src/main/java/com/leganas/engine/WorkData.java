package com.leganas.engine;

import java.util.ArrayList;

/**
 * Created by AndreyLS on 08.02.2017.
 */
public class WorkData {
    private ArrayList<NetClientCard> onlineDriver;
    private ArrayList<NetClientCard> onlineUsers;

    public WorkData() {
        onlineDriver = new ArrayList<>();
        onlineUsers = new ArrayList<>();
    }

    public ArrayList<NetClientCard> getOnlineDriver() {
        return onlineDriver;
    }

    public void setOnlineDriver(ArrayList<NetClientCard> onlineDriver) {
        this.onlineDriver = onlineDriver;
    }

    public ArrayList<NetClientCard> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(ArrayList<NetClientCard> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
}
