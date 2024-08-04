package code.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import code.api.SteamAPI;
import code.games.Game;
import code.games.PhysicalGame;

public class Window extends JFrame {

    // Timer for refreshing visuals within the Window
    private static class WindowTimer extends Timer {

        // Listener for executing all tasks that need to be executed between frames
        private static class WindowListener implements ActionListener {

            // Store tasks & the Window
            private HashMap<String, Runnable> tasks = new HashMap<>();
            private Set<String> keys = tasks.keySet();
            private Window parent;

            // Constructor
            private WindowListener(Window parent) {this.parent = parent;}
            
            // Add a task to the list
            private void putTask(String key, Runnable task) {tasks.put(key, task);}

            // Remove the task with a given key
            private void removeTask(String key) {tasks.remove(key);}

            // The main loop
            public void actionPerformed(ActionEvent e) {
                // Do all tasks
                for (String k : keys) {tasks.get(k).run();}
                // Repaint the Window
                parent.repaint();
            }
        }

        // Store a reference to the listener
        private WindowListener listener;

        // Constructor
        private WindowTimer(int fps, ActionListener listener) {
            super(1000 / fps, listener);
            this.listener = (WindowListener) listener;
        }

        // Access promotion from the WindowListener
        private void putTask(String key, Runnable task) {listener.putTask(key, task);}
        private void removeTask(String key) {listener.removeTask(key);}
    }

    // Window variables
    private Dimension screenSize;
    private int width, height, x, y;
    private JPanel panel;
    private WindowTimer timer;
    
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

    // Timer stuff
    public void initTimer(int fps) {timer = new WindowTimer(fps, new WindowTimer.WindowListener(this)); timer.stop();}
    public void putTask(String key, Runnable task) {timer.putTask(key, task);}
    public void removeTask(String key) {timer.removeTask(key);}
    public void startTimer() {timer.start();}
    public void stopTimer() {timer.stop();}

    // WIP testing
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Window window = new Window();

                Wheel wheel = new Wheel(window.getPanel());
                double r = Math.min((double) window.width / 2.5, (double) window.height / 2.5);

                wheel.setR(r);
                wheel.setLocation((window.width - wheel.getWidth()) / 2, (window.height - wheel.getHeight()) / 2);

                window.initTimer(60);
                window.putTask("SPIN_TEST", new Runnable() {public void run() {wheel.updateSpin();}});

                ArrayList<Game> games = new ArrayList<>();

                games.add(new PhysicalGame("Super Mario 64", "Nintendo 64"));
                games.add(new PhysicalGame("Super Mario Odyssey", "Nintendo Switch"));
                games.add(new PhysicalGame("Super Mario 64 DS", "Nintendo DS"));
                games.add(new PhysicalGame("Super Mario 3D All Stars", "Nintendo Switch"));
                games.add(new PhysicalGame("Super Mario Bros.", "Nintendo Entertainment System"));

                // games = SteamAPI.getInstance().getGames();

                wheel.buildSlices(games);

                window.add(wheel);
                window.startTimer();
            }
        });
    }
}