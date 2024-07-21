package code.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Arc2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import code.util.Util;

public class Window extends JFrame {
    private Dimension screenSize;
    private int width, height, x, y;
    private JPanel panel;
    
    public Window() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Default sizing / position
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.width / 2; 
        height = screenSize.height / 2;
        x = (int) ((screenSize.width - width) / 2);
        y = (int) ((screenSize.height - height) / 2);
        setLocation(x, y);

        // Establish the panel
        panel = new JPanel();
        panel.setLayout(null);
        super.add(panel);
        panel.setPreferredSize(new Dimension(width, height));
        pack();

        // Make the window appear
        setVisible(true);
    }

    // Add new components to the panel and not the frame
    @Override
    public Component add(Component component) {
        Component c = panel.add(component);
        return c;
    }

    // Transform fixers (swing has weird offsets normally)
    @Override
    public void setSize(int width, int height) {super.setSize(width + 16, height + 39);}
    @Override
    public void setSize(Dimension d) {setSize(d.width, d.height);}
    @Override
    public void setLocation(int x, int y) {super.setLocation(x - 8, y - 16);}
    @Override
    public void setLocation(Point p) {setLocation(p.x, p.y);}

    // WIP testing (will later be moved to separate class(es))
    private static class ArcTest extends JComponent {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            Component parent = getParent();
            
            /*
             * Remember to offset X & Y of arc by -r
             * Arc2D goes from "start -> start + length" instead of "start -> end" 
             */

            setSize(parent.getWidth(), parent.getHeight());

            int sliceCount = 5;
            double dt = (double) 360 / sliceCount;
            double startAngle = 90 - (dt / 2);
            double r = (double) Math.min(parent.getWidth(), parent.getHeight()) / 2; 
            double x = (double) parent.getWidth() / 2;
            double y = (double) parent.getHeight() / 2;

            for (int i = 0; i < sliceCount; i++) {
                Arc2D arc = new Arc2D.Double(x - r, y - r, 2*r, 2*r, startAngle, dt, Arc2D.PIE);
                g2d.setColor(new Color(Util.randInt(16777216)));
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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Window window = new Window();

                ArcTest wheeltester = new ArcTest();
                wheeltester.setSize(window.width, window.height);
                window.add(wheeltester);
            }
        });
    }
}