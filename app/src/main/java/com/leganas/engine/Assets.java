package com.leganas.engine;

import com.leganas.engine.controller.ClientController;
import com.leganas.engine.controller.ServerController;

/**
 * Created by AndreyLS on 15.02.2017.
 */
public class Assets
{
    public static ServerController serverController;
    public static ClientController clientController;

    public static Users users; // Сюда мы считываем все логины и пароли доступные (хотя можно и при запросе чистать каждый раз)
    public static WorkData workData;
}
