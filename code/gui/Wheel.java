package code.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

import javax.swing.JComponent;

import code.util.GraphicsUtil;

public class Wheel extends JComponent {
    Container parent;
    boolean autoTransform = false;
    double x, y, r;
    int n = 50;

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
        parent = getParent();
        if (parent == null) {return;}
        setSize(parent.getSize());
        if (autoTransform) {recalcTransforms();}
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double dt = (double) 360 / n;
        double startAngle = 90 - (dt / 2);

        for (int i = 0; i < n; i++) {
            Arc2D arc = new Arc2D.Double(x - r, y - r, 2*r, 2*r, startAngle, dt, Arc2D.PIE);
            g2d.setColor(GraphicsUtil.colorFilter(GraphicsUtil.randomColor()));
            g2d.fill(arc);
            startAngle += dt;
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
