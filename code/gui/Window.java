package code.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

    public Container getPanel() {return panel;}

    // Transform fixers (swing has weird offsets normally)
    @Override
    public void setSize(int width, int height) {super.setSize(width + 16, height + 39);}
    @Override
    public void setSize(Dimension d) {setSize(d.width, d.height);}
    @Override
    public void setLocation(int x, int y) {super.setLocation(x - 8, y - 16);}
    @Override
    public void setLocation(Point p) {setLocation(p.x, p.y);}

    // WIP testing
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Window window = new Window();

                Wheel wheel = new Wheel(window.getPanel());
                double r = Math.min((double) window.width / 2.5, (double) window.height / 2.5);
                
                wheel.setR(r);
                wheel.setLocation((window.width - wheel.getWidth()) / 2, (window.height - wheel.getHeight()) / 2);

                // wheel.setSize(window.width, window.height);
                window.add(wheel);
            }
        });
    }
}