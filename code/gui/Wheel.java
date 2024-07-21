package code.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

import javax.swing.JComponent;

import code.util.GraphicsUtil;

public class Wheel extends JComponent {
    private final Font font = new Font("Comic Sans MS", Font.LAYOUT_LEFT_TO_RIGHT, 19);
    
    private Container parent;
    private boolean autoTransform = false;
    private double x, y, r;
    private int n = 5;

    public Wheel() {}

    public Wheel(Container parent) {
        this.parent = parent;
        autoTransform = true;
        recalcTransforms();
    }

    private void recalcTransforms() {
        x = (double) parent.getWidth() / 2;
        y = (double) parent.getHeight() / 2;
        r = (double) Math.min(parent.getWidth(), parent.getHeight()) / 2.5;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setFont(font);
        if (parent != getParent()) {parent = getParent(); if(parent == null) {return;}}
        setSize(parent.getSize());
        if (autoTransform) {recalcTransforms();}
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double dt = (double) 360 / n;
        double startAngle = -(dt / 2);

        for (int i = 0; i < n; i++) {
            // The arc
            Arc2D arc = new Arc2D.Double(x - r, y - r, 2*r, 2*r, startAngle, dt, Arc2D.PIE);
            g2d.setColor(GraphicsUtil.colorFilter(GraphicsUtil.randomColor()));
            g2d.fill(arc);

            // The text (only render within the arc slice, then reset the clipping region so the other slices can be drawn)
            g2d.clip(arc);
            g2d.setColor(new Color(0xFFFFFF));
            g2d.drawString("Sample text", (int) (x + r/3), (int) (y + 4.5));
            g2d.setClip(getBounds());

            // Apply rotation about the center
            g2d.rotate(-dt*Math.PI/180, x, y);
        }
        
        /* 
        AffineTransform t = new AffineTransform();
        t.rotate(-Math.PI / 2, x, y);
        Shape s = t.createTransformedShape(arc);
        g2d.setColor(new Color(Util.randInt(16777216)));
        g2d.fill(s);
        */

        //g2d.drawArc(parent.getWidth() / 2, parent.getHeight() / 2, 100, 100, 0, 90);
    }
}
