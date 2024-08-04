package code.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import code.games.Game;
import code.util.GraphicsUtil;

public class Wheel extends JComponent {

    // Class for the slices
    private static class Slice {
        private Game game;
        private Color color;
        private int pos = -1;

        public Slice(Game game) {
            this.game = game;
            color = GraphicsUtil.colorFilter(GraphicsUtil.strToColor(game.getName()));
        }

        public void setPos(int pos) {this.pos = pos;}
        public int getPos() {return pos;}
        public void setColor(Color color) {this.color = color;}
        public Color getColor() {return color;}
        public void setGame(Game game) {this.game = game;}
        public Game getGame() {return game;}
    }

    private Font font = new Font("Comic Sans MS", Font.LAYOUT_LEFT_TO_RIGHT, 19);
    
    private Container parent;
    private ArrayList<Slice> slices = new ArrayList<>();
    private double x, y, r, width, height;
    private double angleOffset = 0;

    public Wheel() {}

    public Wheel(Container parent) {
        this.parent = parent;

        // Proof of concept for optimizing slice drawing
        for (int i = 0; i < 750; i++) {
            Color color = GraphicsUtil.randomColor();
            JComponent component = new JComponent() {
                @Override
                public void paintComponent(Graphics g) {
                    g.setColor(color);
                    g.fillRect(getX(), getY(), getWidth(), getHeight());
                    g.setColor(new Color(0xFFFFFF));
                    g.setFont(font);
                    g.drawString("eat pant", getX(), getY());
                }
            };
            add(component);
            component.setLocation(getX() + GraphicsUtil.randInt(100), getY() + GraphicsUtil.randInt(100));
            component.setSize(50, 50);
        }
    }

    // Build the slices list from the ground up, based on a list of games
    public void buildSlices(ArrayList<Game> games) {
        slices = new ArrayList<>();
        for (Game g : games) {
            Slice slice = new Slice(g);
            slice.setPos(slices.size());
            slices.add(slice);
        }
    }

    public void updateSpin() {
        angleOffset -= 1;
    }

    // Transforms

    public double getR() {return r;}
    public void setR(double r) {this.r = r; setAWidth(2*r); setAHeight(2*r); setSize((int) Math.round(width), (int) Math.round(height));}

    private void setAWidth(double width) {this.width = width; x = width / 2;}
    private void setAHeight(double height) {this.height = height; y = height / 2;}

    public double getAWidth() {return width;}
    public double getAHeight() {return height;}

    @Override
    public void paintComponent(Graphics g) {
        // Setup
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(angleOffset*Math.PI/180, x, y);
        super.paintComponent(g);

        double dt;
        if (slices.size() == 0) {dt = 0;} else {dt = (double) 360 / slices.size();}
        double startAngle = -(dt / 2);

        for (Slice s : slices) {
            // Pre-slice
            Shape clip = g2d.getClip();
            int baseFontSize = font.getSize();

            // The arc
            Arc2D arc = new Arc2D.Double(x - r, y - r, 2*r, 2*r, startAngle, dt, Arc2D.PIE);
            g2d.setColor(s.getColor());
            g2d.fill(arc);
            
            // Restrict drawing region to within the arc
            g2d.clip(arc);

            // Text setup
            String sliceText = s.getGame().getName();
            Rectangle2D textDims = font.getStringBounds(sliceText, g2d.getFontRenderContext());

            // Repeatedly shrink font until it fits within the slice
            while (textDims.getWidth() >= 0.9*r) {if (font.getSize() < 8) {break;}; font = new Font(font.getName(), font.getStyle(), font.getSize() - 1); textDims = font.getStringBounds(sliceText, g2d.getFontRenderContext());}
            g.setFont(font);

            // Draw text
            g2d.setColor(new Color(0xFFFFFF));
            g2d.drawString(sliceText, (int) (x + ((r - textDims.getWidth()) / 1.5)), (int) (y + (textDims.getHeight() / 4)));

            // Reset slice-specific vars
            g2d.setClip(clip);
            font = new Font(font.getName(), font.getStyle(), baseFontSize);
            g.setFont(font);

            // Further rotation about the center
            g2d.rotate(-dt*Math.PI/180, x, y);
        }
    }
}
