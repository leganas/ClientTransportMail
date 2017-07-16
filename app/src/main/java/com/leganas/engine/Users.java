package com.leganas.engine;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by AndreyLS on 16.02.2017.
 */
public class Users {
    public String tbName = "База данных юзеров";
    public ArrayList<UserCard> userList;

    public Users() {
        userList = new ArrayList<>();
    }

    public static class UsersConverter implements JsonSerializer<Users>, JsonDeserializer<Users> {

        @Override
        public Users deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            JsonObject usersObj = object.get("users").getAsJsonObject();
            JsonArray userListJson = usersObj.get("usersList").getAsJsonArray();

            Users users = new Users();
            JsonElement tbNameOb = usersObj.get("tbName");
            users.tbName = tbNameOb.getAsString();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(UserCard.class, new UserCard.UserCardConverter());
            Gson gson = builder.create();
            for (int i=0;i<userListJson.size();i++){
                UserCard card;
                JsonElement element = userListJson.get(i);
                card = gson.fromJson(element, UserCard.class);
                users.userList.add(card);
            }
            return users;
        }

        @Override
        public JsonElement serialize(Users src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            JsonObject usersOb = new JsonObject();
            JsonArray userListJson = new JsonArray();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(UserCard.class, new UserCard.UserCardConverter());
            Gson gson = builder.create();



            for (int i = 0; i < Assets.users.userList.size(); i++){
                userListJson.add(gson.toJsonTree(Assets.users.userList.get(i),UserCard.class));
            }

            usersOb.addProperty("tbName",Assets.users.tbName);
            usersOb.add("usersList",userListJson);
            object.add("users",usersOb);
            return object;
        }
    }


}
