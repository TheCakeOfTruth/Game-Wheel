package code.games;

import java.awt.image.BufferedImage;

import code.util.Util;

public class SteamGame extends Game {
    // Stores the Steam appid
    private int appid;
    
    // Constructor
    public SteamGame(int appid, String name) {super(name); setAppid(appid);}

    // Gets the header image from the steam store page
    public BufferedImage getImage() {
        return Util.getImageFromURL("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/" + appid + "/header.jpg");
    }

    // Getters/setters
    public int getAppid() {return appid;}
    public void setAppid(int appid) {this.appid = appid;}
}
