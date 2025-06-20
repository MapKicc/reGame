// Exit Panel

// Import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;

// Exit panel class
public class ExitPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Asset path
    private String path = "assets/Exit/";

    // Image icons
    private ImageIcon exitBgIcon = new ImageIcon(path + "ExitBG.png");
    private ImageIcon nextBtnIcon = new ImageIcon(path + "nextBtn.png");
    private ImageIcon nextBtnHoveredIcon = new ImageIcon(path + "nextBtnHovered.png");
    private ImageIcon retryBtnIcon = new ImageIcon(path + "retryBtn.png");
    private ImageIcon retryBtnHoveredIcon = new ImageIcon(path + "retryBtnHovered.png");
    private ImageIcon menuBtnIcon = new ImageIcon(path + "menuBtn.png");
    private ImageIcon menuBtnHoveredIcon = new ImageIcon(path + "menuBtnHovered.png");
    private ImageIcon lostIcon = new ImageIcon(path + "lost.png");
    private ImageIcon wonIcon = new ImageIcon(path + "won.png");

    // Images
    private Image exitBgImage = exitBgIcon.getImage();
    private Image nextBtnImage = nextBtnIcon.getImage();
    private Image nextBtnHoveredImage = nextBtnHoveredIcon.getImage();
    private Image retryBtnImage = retryBtnIcon.getImage();
    private Image retryBtnHoveredImage = retryBtnHoveredIcon.getImage();
    private Image menuBtnImage = menuBtnIcon.getImage();
    private Image menuBtnHoveredImage = menuBtnHoveredIcon.getImage();
    private Image lostImage = lostIcon.getImage();
    private Image wonImage = wonIcon.getImage();
    private Image titleImage;

    // Rectangles
    private Rectangle nextPhaseBtnRect;
    private Rectangle restartBtnRect;
    private Rectangle mainMenuBtnRect;

    // Button hover states
    private boolean nextPhaseBtnHover = false;
    private boolean restartBtnHover = false;
    private boolean mainMenuBtnHover = false;
    private boolean btnHovered = false;

    // Sound variables
    private Clip exitSound;

    // Animation variables
    private float bgX = 0;
    private float fade = 255f;

    // State variables
    private boolean running = true;
    private boolean fadeIn = true;
    private boolean fadeOut = false;
    private boolean switchToMenu = false;
    private boolean switchToGameRetry = false;
    private boolean switchToGameNext = false;

    // Constructor
    public ExitPanel() {
        // --- Panel Properties ---
        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setFocusable(true);
        // Add listeners
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Initialize sounds
        try {
            // Exit sound
            File exitSoundFile = new File(path + "ExitMusic.wav");
            AudioInputStream exitAudioStream = AudioSystem.getAudioInputStream(exitSoundFile);
            exitSound = AudioSystem.getClip();
            exitSound.open(exitAudioStream);
            // Play exit sound
            exitSound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print the error
            e.printStackTrace();
        }

        // Initialize rectangles
        nextPhaseBtnRect = new Rectangle();
        restartBtnRect = new Rectangle();
        mainMenuBtnRect = new Rectangle();

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

    // Update panel method
    private void updatePanel() {
        // --- Fade In ---
        if (fadeIn) {
            // Decrease the fade
            fade -= 2.5f;
            // If the fade is less than 0, set it to 0 and stop the fade in transition
            if (fade < 0) {
                fade = 0;
                fadeIn = false;
            }
        }

        // --- Fade Out ---
        if (fadeOut) {
            // Increase the fade
            fade += 10f;
            // If the fade is greater than 255, make a transition to the menu
            if (fade > 255) {
                fade = 255;
                if (switchToMenu || switchToGameRetry || switchToGameNext) {
                    // Stop the running loop
                    running = false;
                    // Stop exit sound
                    exitSound.stop();
                    // Remove this panel
                    Main.frame.remove(this);
                    if (switchToMenu) {
                        // Create the main panel
                        Main.createMainPanel();
                        // Add the main panel to the frame
                        Main.frame.add(Main.mainPanel);
                        // Revalidate and repaint the frame
                        Main.frame.revalidate();
                        Main.frame.repaint();
                        // Request focus to make keyboard and mouse work
                        Main.mainPanel.requestFocusInWindow();
                    } else if (switchToGameRetry) {
                        // Create the game panel
                        Main.createGamePanel();
                        // Add the game panel to the frame
                        Main.frame.add(Main.gamePanel);
                        // Revalidate and repaint the frame
                        Main.frame.revalidate();
                        Main.frame.repaint();
                        // Request focus to make keyboard and mouse work
                        Main.gamePanel.requestFocusInWindow();
                    } else if (switchToGameNext) {
                        // Next phase
                        Game.nextPhase();
                        // Create the game panel
                        Main.createGamePanel();
                        // Add the game panel to the frame
                        Main.frame.add(Main.gamePanel);
                        // Revalidate and repaint the frame
                        Main.frame.revalidate();
                        Main.frame.repaint();
                        // Request focus to make keyboard and mouse work
                        Main.gamePanel.requestFocusInWindow();
                    }
                    return;
                }
            }
        }

        // --- Background Animation ---
        // Move background to the left
        bgX -= 0.25f;
        // Reset background position
        if (bgX <= -Main.WIDTH) {
            bgX = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // --- Graphics2D ---
        Graphics2D g2d = (Graphics2D) g;

        // --- Background ---
        g.drawImage(exitBgImage, (int) (bgX), 0, Main.WIDTH, Main.HEIGHT, this);
        g.drawImage(exitBgImage, (int) (bgX) + Main.WIDTH, 0, Main.WIDTH, Main.HEIGHT, this);

        // --- Button Properties ---
        int buttonWidth = Scale.scaleX(nextBtnImage.getWidth(this) / 2);
        int buttonHeight = Scale.scaleY(nextBtnImage.getHeight(this) / 2);
        int buttonSpacing = Scale.scaleY(25);
        int centerX = Main.WIDTH / 2 - buttonWidth / 2;
        int startY = Main.HEIGHT / 2 - Scale.scaleY(50);
        int hoverPos = Scale.scale(5);
        int hoverDim = hoverPos * 2;

        // Check if the player is on the last phase and set the button positions
        if (Game.currentPhase < 3) {
            nextPhaseBtnRect.setBounds(centerX, startY, buttonWidth, buttonHeight);
            restartBtnRect.setBounds(centerX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
            mainMenuBtnRect.setBounds(centerX, startY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
        } else {
            restartBtnRect.setBounds(centerX, startY, buttonWidth, buttonHeight);
            mainMenuBtnRect.setBounds(centerX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        }

        // --- Title Image ---
        // Check if the player won or lost
        if (Main.gamePanel.winner) {
            titleImage = wonImage;
        } else {
            titleImage = lostImage;
        }
        int titleWidth = Scale.scaleX((int) (titleImage.getWidth(this) / 1.2));
        int titleHeight = Scale.scaleY((int) (titleImage.getHeight(this) / 1.2));
        g.drawImage(titleImage, (Main.WIDTH - titleWidth) / 2, Main.HEIGHT / 2 - titleHeight - Scale.scaleY(150),
                titleWidth, titleHeight, this);

        // --- Buttons ---
        // Check if the player is on the last phase
        if (Game.currentPhase < 3) {
            // If the player won
            if (Main.gamePanel.winner) {
                // If the button is hovered
                if (nextPhaseBtnHover) {
                    g.drawImage(nextBtnHoveredImage, nextPhaseBtnRect.x - hoverPos, nextPhaseBtnRect.y - hoverPos,
                            nextPhaseBtnRect.width + hoverDim, nextPhaseBtnRect.height + hoverDim, this);
                } else {
                    g.drawImage(nextBtnImage, nextPhaseBtnRect.x, nextPhaseBtnRect.y, nextPhaseBtnRect.width,
                            nextPhaseBtnRect.height, this);
                }
            }
            // Draw inactive next button
            else {
                // Set the alpha
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2d.drawImage(nextBtnImage, nextPhaseBtnRect.x, nextPhaseBtnRect.y,
                        nextPhaseBtnRect.width, nextPhaseBtnRect.height, this);
                // Reset the alpha
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }

        // Restart button
        if (restartBtnHover) {
            g.drawImage(retryBtnHoveredImage, restartBtnRect.x - hoverPos, restartBtnRect.y - hoverPos,
                    restartBtnRect.width + hoverDim, restartBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(retryBtnImage, restartBtnRect.x, restartBtnRect.y,
                    restartBtnRect.width, restartBtnRect.height, this);
        }

        // Main menu button
        if (mainMenuBtnHover) {
            g.drawImage(menuBtnHoveredImage, mainMenuBtnRect.x - hoverPos, mainMenuBtnRect.y - hoverPos,
                    mainMenuBtnRect.width + hoverDim, mainMenuBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(menuBtnImage, mainMenuBtnRect.x, mainMenuBtnRect.y,
                    mainMenuBtnRect.width, mainMenuBtnRect.height, this);
        }

        // --- Fade Overlay ---
        g.setColor(new Color(0, 0, 0, (int) fade));
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
    }

    // Mouse pressed method
    @Override
    public void mousePressed(MouseEvent e) {
        // Next phase button
        if (nextPhaseBtnRect.contains(e.getPoint()) && Game.currentPhase < 3 && Main.gamePanel.winner) {
            Main.playClickSound();
            // Fade out and switch to the next phase
            fadeOut = true;
            switchToGameNext = true;
        }
        // Restart button
        if (restartBtnRect.contains(e.getPoint())) {
            Main.playClickSound();
            // Fade out and switch to the game retry
            fadeOut = true;
            switchToGameRetry = true;
        }
        // Main menu button
        if (mainMenuBtnRect.contains(e.getPoint())) {
            Main.playClickSound();
            // Fade out and switch to the menu
            fadeOut = true;
            switchToMenu = true;
        }
    }

    // Mouse moved method
    @Override
    public void mouseMoved(MouseEvent e) {
        // Store the old hovered state
        boolean btnOldHovered = btnHovered;

        // Check if the mouse is hovering over any button
        nextPhaseBtnHover = nextPhaseBtnRect.contains(e.getPoint()) && Game.currentPhase < 3 && Main.gamePanel.winner;
        restartBtnHover = restartBtnRect.contains(e.getPoint());
        mainMenuBtnHover = mainMenuBtnRect.contains(e.getPoint());

        // If any button is hovered, set btnHovered to true
        btnHovered = nextPhaseBtnHover || restartBtnHover || mainMenuBtnHover;

        // If any button is hovered and the old hovered state is false, play hover sound
        if (btnHovered && !btnOldHovered) {
            Main.playHoverSound();
        }
    }

    // Unused methods
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
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}