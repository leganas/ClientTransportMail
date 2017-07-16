package com.leganas.engine;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by AndreyLS on 15.02.2017.
 */
public class UserCard {
    public enum Permission {
        Guest,
        ServerAdmin,
        MobileServerAdmin,
        MobileDriver,
        MobileUser
    }

    int net_id;
    String login, password;
    String name;
    String photo_url;
    Permission permission;

    public static class UserCardConverter implements JsonSerializer<UserCard>, JsonDeserializer<UserCard> {

        @Override
        public UserCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            int net_id = Integer.parseInt(object.get("net_id").getAsString());
            String login = object.get("login").getAsString();
            String password = object.get("password").getAsString();
            String name = object.get("name").getAsString();
            // проверка наличия поля
            // if (object.has("photo_url"))
            String photo_url = object.get("photo_url").getAsString();
            String permission = object.get("permission").getAsString();
            Permission permissionEnum;
            switch(permission) {
                case "ServerAdmin":permissionEnum = Permission.ServerAdmin;
                    break;
                case "MobileServerAdmin":permissionEnum = Permission.MobileServerAdmin;
                    break;
                case "MobileDriver":permissionEnum = Permission.MobileDriver;
                    break;
                case "MobileUser":permissionEnum = Permission.MobileUser;
                    break;
                default: permissionEnum = Permission.Guest;
                    break;
            }
            return new UserCard(net_id,login,password,name,photo_url,permissionEnum);
        }

        @Override
        public JsonElement serialize(UserCard src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("net_id", src.getNet_id());
            object.addProperty("login", src.getLogin());
            object.addProperty("password", src.getPassword());
            object.addProperty("name", src.getName());
            object.addProperty("photo_url", src.getPhoto_url());
            object.addProperty("permission", src.getPermission().toString());
            return object;
        }
    }

    public UserCard() {
    }

    public UserCard(int net_id, String login, String password, String name, String photo_url, Permission permission) {
        this.net_id = net_id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.photo_url = photo_url;
        this.permission = permission;
    }

    public int getNet_id() {
        return net_id;
    }

    public void setNet_id(int net_id) {
        this.net_id = net_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
