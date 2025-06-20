// Main panel

// Import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.Random;

// Main panel class
public class MainPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Random
    private Random random = new Random();

    // Sound variables
    private Clip mainMenuMusic;

    // Asset path
    private String path = "assets/MainMenu/";

    // Image icons
    private ImageIcon currentPhaseIcon;
    private ImageIcon currentSpriteIcon;
    private ImageIcon introBgIcon = new ImageIcon(path + "MainMenuBG.png");
    private ImageIcon logoIcon = new ImageIcon(path + "logo.png");
    private ImageIcon optionsBtnIcon = new ImageIcon(path + "optionsBtn.png");
    private ImageIcon optionsBtnHoveredIcon = new ImageIcon(path + "optionsBtnHovered.png");
    private ImageIcon phaseBtnIcon = new ImageIcon(path + "phaseBtn.png");
    private ImageIcon phaseBtnHoveredIcon = new ImageIcon(path + "phaseBtnHovered.png");
    private ImageIcon playerBtnIcon = new ImageIcon(path + "playerBtn.png");
    private ImageIcon playerBtnHoveredIcon = new ImageIcon(path + "playerBtnHovered.png");
    private ImageIcon quitBtnIcon = new ImageIcon(path + "quitBtn.png");
    private ImageIcon quitBtnHoveredIcon = new ImageIcon(path + "quitBtnHovered.png");
    private ImageIcon startBtnIcon = new ImageIcon(path + "playBtn.png");
    private ImageIcon startBtnHoveredIcon = new ImageIcon(path + "playBtnHovered.png");

    // Images
    private Image currentPhaseImage;
    private Image currentSpriteImage;
    private Image introBgImage = introBgIcon.getImage();
    private Image logoImage = logoIcon.getImage();
    private Image optionsBtnImage = optionsBtnIcon.getImage();
    private Image optionsBtnHoveredImage = optionsBtnHoveredIcon.getImage();
    private Image phaseBtnImage = phaseBtnIcon.getImage();
    private Image phaseBtnHoveredImage = phaseBtnHoveredIcon.getImage();
    private Image playerBtnImage = playerBtnIcon.getImage();
    private Image playerBtnHoveredImage = playerBtnHoveredIcon.getImage();
    private Image quitBtnImage = quitBtnIcon.getImage();
    private Image quitBtnHoveredImage = quitBtnHoveredIcon.getImage();
    private Image startBtnImage = startBtnIcon.getImage();
    private Image startBtnHoveredImage = startBtnHoveredIcon.getImage();

    // Rectangles
    private Rectangle optionsBtnRect;
    private Rectangle phaseBtnRect;
    private Rectangle playerBtnRect;
    private Rectangle quitBtnRect;
    private Rectangle startBtnRect;

    // Button hover states
    private boolean optionsBtnHover = false;
    private boolean phaseBtnHover = false;
    private boolean playerBtnHover = false;
    private boolean quitBtnHover = false;
    private boolean startBtnHover = false;
    private boolean btnHovered = false;

    // Panels
    private Options options;
    private PhaseSelection phaseSelection;
    private SpriteSelection spriteSelection;

    // Animation variables
    private float bgX = 0;
    private float fade = 255f;

    // State variables
    private boolean fadeIn = true;
    private boolean fadeOut = false;
    private boolean running = true;
    private boolean switchToExit = false;
    private boolean switchToGame = false;

    // Constructor
    public MainPanel() {
        // --- Panel Properties ---
        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setFocusable(true);
        // Add listeners
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Initialize rectangles
        startBtnRect = new Rectangle();
        optionsBtnRect = new Rectangle();
        quitBtnRect = new Rectangle();
        playerBtnRect = new Rectangle();
        phaseBtnRect = new Rectangle();

        // Initialize panels
        spriteSelection = new SpriteSelection();
        phaseSelection = new PhaseSelection();
        options = new Options();

        // Play main menu music
        playMainMenuMusic();

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
        // Button positioning
        int btnWidth = Scale.scaleX(270);
        int btnHeight = Scale.scaleY(125);
        int btnSpacing = Scale.scaleY(25);
        int centerX = Main.WIDTH / 2;

        // Main buttons properties
        int totalHeight = (3 * btnHeight) + (2 * btnSpacing);
        int btnStartY = (Main.HEIGHT - totalHeight) / 2 + Scale.scaleY(100);
        int btnX = centerX - btnWidth / 2;

        // Set new bounds for buttons
        startBtnRect.setBounds(btnX, btnStartY, btnWidth, btnHeight);
        optionsBtnRect.setBounds(btnX, btnStartY + btnHeight + btnSpacing, btnWidth, btnHeight);
        quitBtnRect.setBounds(btnX, btnStartY + 2 * (btnHeight + btnSpacing), btnWidth, btnHeight);

        // Side buttons properties
        int sideBtnWidth = Scale.scaleX(185);
        int sideBtnHeight = Scale.scaleY(88);
        int sideBtnY = Main.HEIGHT / 2 - sideBtnHeight / 2 + Scale.scaleY(150);
        int sideBtnSpacing = Scale.scaleX(200);

        // Set new bounds for side buttons
        playerBtnRect.setBounds(centerX - btnWidth / 2 - sideBtnSpacing - sideBtnWidth, sideBtnY, sideBtnWidth,
                sideBtnHeight);
        phaseBtnRect.setBounds(centerX + btnWidth / 2 + sideBtnSpacing, sideBtnY, sideBtnWidth, sideBtnHeight);

        // Update current phase and sprite
        currentPhaseIcon = new ImageIcon("assets/Global/phases/phase" + Game.currentPhase + ".png");
        currentSpriteIcon = new ImageIcon(
                "assets/Global/sprites/" + Game.currentSprite + "/" + Game.currentSprite + "Rest.png");
        currentPhaseImage = currentPhaseIcon.getImage();
        currentSpriteImage = currentSpriteIcon.getImage();

        // Fade in transition
        if (fadeIn) {
            // Decrease the fade
            fade -= 2.5f;
            // If the fade is less than 0, set it to 0 and stop the fade in transition
            if (fade < 0) {
                fade = 0;
                fadeIn = false;
            }
        }

        // Fade out transition
        if (fadeOut) {
            // Increase the fade
            fade += 10f;
            // If the fade is greater than 255, make a transition to the game
            if (fade > 255) {
                fade = 255;
                if (switchToGame) {
                    // Stop the running loop
                    running = false;
                    mainMenuMusic.stop();
                    mainMenuMusic.close();
                    // Remove this panel
                    Main.frame.remove(this);
                    // Create the game panel
                    Main.createGamePanel();
                    // Add the game panel to the frame
                    Main.frame.add(Main.gamePanel);
                    // Revalidate and repaint the frame
                    Main.frame.revalidate();
                    Main.frame.repaint();
                    // Request focus to make keyboard and mouse work
                    Main.gamePanel.requestFocusInWindow();
                    return;
                } else if (switchToExit) {
                    System.exit(0);
                }
            }
        }

        // Move background to the left
        bgX -= 0.25;
        // Reset background position
        if (bgX <= -Main.WIDTH) {
            bgX = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // --- Background ---
        g.drawImage(introBgImage, (int) (bgX), 0, Main.WIDTH, Main.HEIGHT, this);
        g.drawImage(introBgImage, (int) (bgX) + Main.WIDTH, 0, Main.WIDTH, Main.HEIGHT, this);

        // --- Logo ---
        int logoHeight = Scale.scaleY((int) (logoImage.getHeight(this) / 1.3));
        int logoWidth = Scale.scaleX((int) (logoImage.getWidth(this) / 1.1));
        g.drawImage(logoImage, (Main.WIDTH - logoWidth) / 2,
                (Main.HEIGHT - logoHeight) / 2 - Scale.scaleY(300), logoWidth, logoHeight, this);

        // --- Main Buttons ---
        // Hover effect properties
        int hoverPos = Scale.scale(5);
        int hoverDim = hoverPos * 2;

        // Draw start button
        if (startBtnHover) {
            g.drawImage(startBtnHoveredImage, startBtnRect.x - hoverPos, startBtnRect.y - hoverPos,
                    startBtnRect.width + hoverDim, startBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(startBtnImage, startBtnRect.x, startBtnRect.y, startBtnRect.width,
                    startBtnRect.height, this);
        }

        // Draw options button
        if (optionsBtnHover) {
            g.drawImage(optionsBtnHoveredImage, optionsBtnRect.x - hoverPos, optionsBtnRect.y - hoverPos,
                    optionsBtnRect.width + hoverDim, optionsBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(optionsBtnImage, optionsBtnRect.x, optionsBtnRect.y, optionsBtnRect.width,
                    optionsBtnRect.height, this);
        }

        // Draw quit button
        if (quitBtnHover) {
            g.drawImage(quitBtnHoveredImage, quitBtnRect.x - hoverPos, quitBtnRect.y - hoverPos,
                    quitBtnRect.width + hoverDim, quitBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(quitBtnImage, quitBtnRect.x, quitBtnRect.y, quitBtnRect.width, quitBtnRect.height, this);
        }

        // --- Side Buttons ---
        // Calculate sprite position with scaling
        int spriteWidth = Scale.scaleX((int) (currentSpriteImage.getWidth(this) / 3.5));
        int spriteHeight = Scale.scaleY((int) (currentSpriteImage.getHeight(this) / 3.5));
        int spriteX = playerBtnRect.x + (playerBtnRect.width - spriteWidth) / 2;
        int spriteY = playerBtnRect.y - spriteHeight - Scale.scaleY(10);

        // Draw player button
        if (playerBtnHover) {
            g.drawImage(playerBtnHoveredImage, playerBtnRect.x - hoverPos, playerBtnRect.y - hoverPos,
                    playerBtnRect.width + hoverDim, playerBtnRect.height + hoverDim, this);
            // Move sprite up
            spriteY = playerBtnRect.y - spriteHeight - Scale.scaleY(12);
        } else {
            g.drawImage(playerBtnImage, playerBtnRect.x, playerBtnRect.y, playerBtnRect.width, playerBtnRect.height,
                    this);
        }

        // Draw sprite
        g.drawImage(currentSpriteImage, spriteX, spriteY, spriteWidth, spriteHeight, this);

        // Draw current phase image above phase button
        int phaseSize = Scale.scaleX(80);
        int phaseX = phaseBtnRect.x + phaseBtnRect.width / 2 - phaseSize / 2;
        int phaseY = phaseBtnRect.y - phaseSize - Scale.scaleY(20);

        // Draw phase button
        if (phaseBtnHover) {
            g.drawImage(phaseBtnHoveredImage, phaseBtnRect.x - hoverPos, phaseBtnRect.y - hoverPos,
                    phaseBtnRect.width + hoverDim, phaseBtnRect.height + hoverDim, this);
            // Move phase image up
            phaseY = phaseBtnRect.y - phaseSize - Scale.scaleY(22);
        } else {
            g.drawImage(phaseBtnImage, phaseBtnRect.x, phaseBtnRect.y,
                    phaseBtnRect.width, phaseBtnRect.height, this);
        }

        // Draw phase image
        g.drawImage(currentPhaseImage, phaseX, phaseY, phaseSize, phaseSize, this);

        // --- Version Text ---
        // Set custom font
        try {
            g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/Global/mainFont.ttf")).deriveFont(Font.PLAIN,
                    Scale.scale(24)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Draw version text
        g.setColor(Color.WHITE);
        g.drawString("v1.0.0 (BETA)", Scale.scaleX(20), Main.HEIGHT - Scale.scaleY(15));

        // --- Options Panel ---
        // Use paint component from options panel to draw
        options.paintComponent(g);

        // --- Sprite Selection Panel ---
        // Use paint component from sprite selection panel to draw
        spriteSelection.paintComponent(g);

        // --- Phase Selection Panel ---
        // Use paint component from phase selection panel to draw
        phaseSelection.paintComponent(g);

        // --- Fade Overlay ---
        if (fade > 0) {
            g.setColor(new Color(0, 0, 0, (int) fade));
            g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        }
    }

    // Play main menu music
    private void playMainMenuMusic() {
        // Initialize string variable
        String randMusic = "";

        // Randomly select one of the music files
        int randIndex = random.nextInt(2) + 1;
        if (randIndex == 1) {
            randMusic = "MainMenuMusic1.wav";
        } else {
            randMusic = "MainMenuMusic2.wav";
        }

        // Initialize sound
        try {
            // Main menu music
            File mainMenuMusicFile = new File(path + randMusic);
            AudioInputStream mainMenuAudioStream = AudioSystem.getAudioInputStream(mainMenuMusicFile);
            mainMenuMusic = AudioSystem.getClip();
            mainMenuMusic.open(mainMenuAudioStream);
            // Loop the music
            mainMenuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print the error
            e.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Options panel
        if (options.visible) {
            // Call the mouse pressed method from the options panel
            options.mousePressed(e);
        }
        // Sprite selection panel
        if (spriteSelection.visible) {
            // Call the mouse pressed method from the sprite selection panel
            spriteSelection.mousePressed(e);
        }
        // Phase selection panel
        if (phaseSelection.visible) {
            // Call the mouse pressed method from the phase selection panel
            phaseSelection.mousePressed(e);
        }
        // Main menu
        if (!options.visible && !spriteSelection.visible && !phaseSelection.visible) {
            // Start button
            if (startBtnRect.contains(e.getPoint()) && !fadeOut) {
                // Switch to game
                Main.playClickSound();
                fadeOut = true;
                switchToGame = true;
            }
            // Options button
            if (optionsBtnRect.contains(e.getPoint())) {
                Main.playClickSound();
                options.visible = true;
            }
            // Player button
            if (playerBtnRect.contains(e.getPoint())) {
                Main.playClickSound();
                spriteSelection.visible = true;
            }
            // Phase button
            if (phaseBtnRect.contains(e.getPoint())) {
                Main.playClickSound();
                phaseSelection.visible = true;
            }
            // Quit button
            if (quitBtnRect.contains(e.getPoint()) && !fadeOut) {
                // Close the game
                Main.playClickSound();
                fadeOut = true;
                switchToExit = true;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Options panel
        if (options.visible) {
            // Call the mouse moved method from the options panel
            options.mouseMoved(e);
        }
        // Sprite selection panel
        if (spriteSelection.visible) {
            // Call the mouse moved method from the sprite selection panel
            spriteSelection.mouseMoved(e);
        }
        // Phase selection panel
        if (phaseSelection.visible) {
            // Call the mouse moved method from the phase selection panel
            phaseSelection.mouseMoved(e);
        }
        // Main menu
        if (!options.visible && !spriteSelection.visible && !phaseSelection.visible) {
            // Store the old hovered state
            boolean btnOldHovered = btnHovered;

            // Check if the mouse is hovering over any button
            startBtnHover = startBtnRect.contains(e.getPoint());
            optionsBtnHover = optionsBtnRect.contains(e.getPoint());
            quitBtnHover = quitBtnRect.contains(e.getPoint());
            playerBtnHover = playerBtnRect.contains(e.getPoint());
            phaseBtnHover = phaseBtnRect.contains(e.getPoint());

            // If any button is hovered, set btnHovered to true
            btnHovered = startBtnHover || optionsBtnHover || quitBtnHover || playerBtnHover || phaseBtnHover;

            // If any button is hovered and the old hovered state is false, play hover sound
            if (btnHovered && !btnOldHovered) {
                Main.playHoverSound();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Escape key
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // Options panel
            if (options.visible) {
                // Call the key pressed method from the options panel
                options.keyPressed(e);
            }
            // Sprite selection panel
            if (spriteSelection.visible) {
                // Call the key pressed method from the sprite selection panel
                spriteSelection.keyPressed(e);
            }
            // Phase selection panel
            if (phaseSelection.visible) {
                // Call the key pressed method from the phase selection panel
                phaseSelection.keyPressed(e);
            }
        }
        // F11 key
        if (e.getKeyCode() == KeyEvent.VK_F11) {
            // Toggle fullscreen
            Scale.toggleFullscreen();
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
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}