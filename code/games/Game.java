package code.games;

import java.awt.image.BufferedImage;

public abstract class Game {
    // Name of the Game
    private String name;

    // Constructor (sets the name)
    public Game(String name) {setName(name);} 

    // Getters/setters
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    // Abstract image get method
    public abstract BufferedImage getImage();

    public String toString() {return getName();}
}
