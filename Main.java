// Bohdan Teodorovych
// May 19th, 2025
// Final Project

// Import libraries
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;
import java.awt.*;

// Main class
public class Main {
    // JFrame
    public static JFrame frame;

    // Window size variables based on the user's screen size
    public static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    // Game icon
    public static ImageIcon icon = new ImageIcon("assets/Global/icon.png");
    public static Image iconImage = icon.getImage();

    // Panels for different screens
    public static IntroPanel introPanel;
    public static MainPanel mainPanel;
    public static GamePanel gamePanel;
    public static ExitPanel exitPanel;

    // Sound variables
    public static Clip hover;
    public static Clip click;

    // Constructor
    public Main() {
        // Create the frame
        frame = new JFrame();

        // Create intro panel
        createIntroPanel();
        // Add the intro panel
        frame.add(introPanel);

        // Set the title of the window
        frame.setTitle("REGAME");
        // Set the icon of the window
        frame.setIconImage(iconImage);

        // Set the default close operation to exit on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set the window to not be resizable
        frame.setResizable(false);

        // Set the window to be undecorated and maximized
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Pack the frame and make it visible
        frame.pack();
        frame.setVisible(true);

        // Initialize sounds
        try {
            // Hover sound
            File hoverFile = new File("assets/Global/hover.wav");
            AudioInputStream hoverAudioStream = AudioSystem.getAudioInputStream(hoverFile);
            hover = AudioSystem.getClip();
            hover.open(hoverAudioStream);

            // Click sound
            File clickFile = new File("assets/Global/click.wav");
            AudioInputStream clickAudioStream = AudioSystem.getAudioInputStream(clickFile);
            click = AudioSystem.getClip();
            click.open(clickAudioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print the error
            e.printStackTrace();
        }
    }

    // Method to play the hover sound
    static void playHoverSound() {
        hover.stop();
        hover.setFramePosition(0);
        hover.start();
    }

    // Method to play the click sound
    static void playClickSound() {
        click.stop();
        click.setFramePosition(0);
        click.start();
    }

    // Method to create the intro panel
    static void createIntroPanel() {
        introPanel = new IntroPanel();
    }

    // Method to create the main panel
    static void createMainPanel() {
        mainPanel = new MainPanel();
    }

    // Method to create the game panel
    static void createGamePanel() {
        gamePanel = new GamePanel();
    }

    // Method to create the exit panel
    static void createExitPanel() {
        exitPanel = new ExitPanel();
    }

    // Main method to start the game
    public static void main(String[] args) {
        new Main();
    }
}