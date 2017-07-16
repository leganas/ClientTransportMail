package com.leganas.engine;

/**
 * Created by AndreyLS on 18.02.2017.
 */
public class NetClientCard {
    int net_id;
    String name;
    String photo_url;
    UserCard.Permission permission;

    public static class Possition{
        double latitude; double longitude;
    }

    Possition youPossinion, startPossition,finishPossition;

    public int getNet_id() {
        return net_id;
    }

    public void setNet_id(int net_id) {
        this.net_id = net_id;
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

    public UserCard.Permission getPermission() {
        return permission;
    }

    public void setPermission(UserCard.Permission permission) {
        this.permission = permission;
    }

    public Possition getYouPossinion() {
        return youPossinion;
    }

    public void setYouPossinion(Possition youPossinion) {
        this.youPossinion = youPossinion;
    }

    public Possition getStartPossition() {
        return startPossition;
    }

    public void setStartPossition(Possition startPossition) {
        this.startPossition = startPossition;
    }

    public Possition getFinishPossition() {
        return finishPossition;
    }

    public void setFinishPossition(Possition finishPossition) {
        this.finishPossition = finishPossition;
    }
}
