package code.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import code.util.GraphicsUtil;

public class Wheel extends JComponent {
    private Font font = new Font("Comic Sans MS", Font.LAYOUT_LEFT_TO_RIGHT, 19);
    
    private Container parent;
    private double x, y, r, width, height;
    private int n = 5;

    public Wheel() {}

    public Wheel(Container parent) {
        this.parent = parent;
    }

    public double getR() {return r;}
    public void setR(double r) {this.r = r; setAWidth(2*r); setAHeight(2*r); setSize((int) Math.round(width), (int) Math.round(height));}

    private void setAWidth(double width) {this.width = width; x = width / 2;}
    private void setAHeight(double height) {this.height = height; y = height / 2;}

    public double getAWidth() {return width;}
    public double getAHeight() {return height;}

    @Override
    public void paintComponent(Graphics g) {
        // Setup
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double dt = (double) 360 / n;
        double startAngle = -(dt / 2);

        for (int i = 0; i < n; i++) {
            // Pre-slice
            Shape clip = g2d.getClip();
            int baseFontSize = font.getSize();

            // The arc
            Arc2D arc = new Arc2D.Double(x - r, y - r, 2*r, 2*r, startAngle, dt, Arc2D.PIE);
            g2d.setColor(GraphicsUtil.colorFilter(GraphicsUtil.randomColor()));
            g2d.fill(arc);
            
            // Restrict drawing region to within the arc
            g2d.clip(arc);

            // Text setup
            String sliceText = "Sample Text";
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
