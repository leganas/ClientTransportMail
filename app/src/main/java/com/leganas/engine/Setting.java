package com.leganas.engine;

/**
 * Created by AndreyLS on 14.02.2017.
 */
public class Setting {
    public enum ProgramType {
        Server,
        CientServer,
        Client
    }


    public enum StatusClient {
        off,
        init,
        run,
        dispose,
        turn
    }

    public static ProgramType programType;
    public static String ServerName = "TransportServer";
    public static String IPAdressFromClient = "localhost";
}
