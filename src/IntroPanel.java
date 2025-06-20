// Intro Panel

// Import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

// Intro panel class
public class IntroPanel extends JPanel implements MouseListener, KeyListener {

    // Image variables
    private ImageIcon logoIcon = new ImageIcon("assets/Intro/restudio.png");
    private Image logoImage = logoIcon.getImage();

    // Sound variables
    private Clip introSound;

    // Animation variables
    private float fade = 255f;
    private float scale = 0f;

    // State variables
    private boolean fadeIn = true;
    private boolean fadeOut = false;
    private boolean running = true;
    private boolean switchToMenu = false;

    // Constructor
    public IntroPanel() {
        // Set up the panel
        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);

        // Initialize sounds
        try {
            // Intro sound
            File introSoundFile = new File("assets/Intro/introSound.wav");
            AudioInputStream introSoundAudioStream = AudioSystem.getAudioInputStream(introSoundFile);
            introSound = AudioSystem.getClip();
            introSound.open(introSoundAudioStream);
            // Play sound
            introSound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print the error
            e.printStackTrace();
        }

        // Start animation
        startAnimation();
    }

    // Start animation method
    private void startAnimation() {
        // Create the animation thread
        Thread animationThread = new Thread(() -> {
            // While the game is running
            while (running) {
                // Update the game
                updatePanel();
                // Repaint the panel
                repaint();
                // Sleep for 16ms
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    // Break the loop if interrupted
                    break;
                }
            }
        });
        // Start the animation thread
        animationThread.start();
    }

    // Update game method
    private void updatePanel() {
        // Fade in transition
        if (fadeIn) {
            // Decrease the fade
            fade -= 1f;
            // If the fade is less than 0, set it to 0 and stop the fade-in transition
            if (fade < 0) {
                fade = 0;
                fadeIn = false;
            }
        }

        // Fade out transition
        if (fadeOut) {
            // Increase the fade
            fade += 5f;
            // If the fade is greater than 255, make a transition to the menu
            if (fade > 255) {
                fade = 255;
                if (switchToMenu) {
                    // Stop the running loop
                    running = false;
                    // Remove this panel
                    Main.frame.remove(this);
                    // Create the main panel
                    Main.createMainPanel();
                    // Add the main panel to the frame
                    Main.frame.add(Main.mainPanel);
                    // Revalidate and repaint the frame
                    Main.frame.revalidate();
                    Main.frame.repaint();
                    // Request focus to make keyboard and mouse work
                    Main.mainPanel.requestFocusInWindow();
                    return;
                }
            }
        }

        // Scale the logo
        scale += 0.002f;
        // Switch to menu when scale is 1
        if (scale >= 1f) {
            fadeOut = true;
            switchToMenu = true;
        }
    }

    // Paint component method
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

        // Draw logo
        int logoWidth = (int) (logoImage.getWidth(this) * scale);
        int logoHeight = (int) (logoImage.getHeight(this) * scale);
        g.drawImage(logoImage, (Main.WIDTH - logoWidth) / 2,
                (Main.HEIGHT - logoHeight) / 2, logoWidth, logoHeight, this);

        // Draw fade overlay
        g.setColor(new Color(0, 0, 0, (int) fade));
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Switch to menu when left mouse button is pressed
        if (e.getButton() == MouseEvent.BUTTON1 && !fadeOut) {
            fadeOut = true;
            switchToMenu = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Switch to menu when space is pressed
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !fadeOut) {
            fadeOut = true;
            switchToMenu = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}