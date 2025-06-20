// Scale Manager

// Import libraries
import javax.swing.*;
import java.awt.*;

// Scale manager class
public class Scale {
    // Base design resolution
    public static final int BASE_WIDTH = 1920;
    public static final int BASE_HEIGHT = 1080;

    // Fullscreen state
    public static boolean fullscreen = true;

    // Method to toggle fullscreen
    public static void toggleFullscreen() {
        // Dispose frame
        Main.frame.dispose();

        if (fullscreen) {
            // --- Windowed Mode ---
            fullscreen = false;
            // Set windowed dimensions
            Main.WIDTH = 1280;
            Main.HEIGHT = 720;
            // Enable window decorations
            Main.frame.setUndecorated(false);
            // Set normal window state
            Main.frame.setExtendedState(JFrame.NORMAL);
        } else {
            // --- Fullscreen Mode ---
            fullscreen = true;
            // Set screen dimensions
            Main.WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
            Main.HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
            // Disable window decorations
            Main.frame.setUndecorated(true);
            // Maximize window
            Main.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        // Get the content pane and set the preferred size
        Main.frame.getContentPane().setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        // Pack the frame
        Main.frame.pack();
        // Set the location of the frame to the center of the screen
        Main.frame.setLocationRelativeTo(null);
        // Set the frame to visible
        Main.frame.setVisible(true);
    }

    // Scale X value
    public static int scaleX(int value) {
        return (int) (value * (double) Main.WIDTH / BASE_WIDTH);
    }

    // Scale Y value
    public static int scaleY(int value) {
        return (int) (value * (double) Main.HEIGHT / BASE_HEIGHT);
    }

    // Scale value uniformly
    public static int scale(int value) {
        double scaleX = (double) Main.WIDTH / BASE_WIDTH;
        double scaleY = (double) Main.HEIGHT / BASE_HEIGHT;
        return (int) (value * Math.min(scaleX, scaleY));
    }
}
