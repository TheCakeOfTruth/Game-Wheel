import java.awt.Desktop;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import javax.imageio.ImageIO;

import code.api.SteamAPI;
import code.games.PhysicalGame;
import code.games.SteamGame;

public class Main {
    public static void main(String[] args) throws Exception {
        //System.out.println(SteamAPI.getInstance().getGames());
        //ImageIO.write(new PhysicalGame("Undertale", "Nintendo Switch").getImage(), "png", "test.png");
        File file = new File("test.png");
        ImageIO.write(new PhysicalGame("My Stop Smoking Coach", "Nintendo DS").getImage(), "png", file);
        Desktop.getDesktop().open(file);

        // Sample program: gets the link to the header image of a random game in my steam library

        /*
        ArrayList<SteamGame> games = SteamAPI.getInstance().getGames();
        Random r = new Random();
        int ri = Math.abs(r.nextInt() % games.size());
        System.out.println(ri);
        for (int i = 0; i < games.size(); i++) {
            if(i == ri) {
                System.out.println("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/" + games.get(i).getAppid() + "/header.jpg");
            }
        }
        */
    }
}