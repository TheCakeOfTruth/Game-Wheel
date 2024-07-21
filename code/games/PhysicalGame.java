package code.games;

import java.awt.image.BufferedImage;

import code.api.GamesDBScraper;
import code.util.WebUtil;

public class PhysicalGame extends Game {
    private String platform = "";

    public PhysicalGame(String name, String platform) {super(name); setPlatform(platform);}

    public BufferedImage getImage() {return WebUtil.getImageFromURL(GamesDBScraper.getInstance().getCoverImageURL(getName(), getPlatform()));}

    public String getPlatform() {return platform;}
    public void setPlatform(String platform) {this.platform = platform;}
}
