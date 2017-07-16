package com.leganas.engine;

/**
 * Created by AndreyLS on 16.02.2017.
 */
public class Status {
    public enum StatusServer {
        off,
        init,
        run,
        dispose,
        turn
    }

    public static StatusServer statusServer = StatusServer.off;

    public enum ClientStatus{
        offline,
        online,
        dispose
    }
    public static ClientStatus clientStatus = ClientStatus.offline;

}
