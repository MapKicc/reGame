import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    // Random
    private Random random = new Random();

    // Asset path
    private String path = "assets/Game/";

    // World size
    private static int WORLD_WIDTH;
    private static int WORLD_HEIGHT;

    // Image icons
    private ImageIcon currentSpriteRestIcon = new ImageIcon(
            "assets/Global/sprites/" + Game.currentSprite + "/" + Game.currentSprite + "Rest.png");
    private ImageIcon currentSpriteMoving1Icon = new ImageIcon(
            "assets/Global/sprites/" + Game.currentSprite + "/" + Game.currentSprite + "Moving1.png");
    private ImageIcon currentSpriteMoving2Icon = new ImageIcon(
            "assets/Global/sprites/" + Game.currentSprite + "/" + Game.currentSprite + "Moving2.png");
    private ImageIcon bgIcon = new ImageIcon(path + "bg.png");
    private ImageIcon currentPhaseIcon = new ImageIcon("assets/Global/phases/phase" + Game.currentPhase + ".png");

    // Images
    private Image bgImage = bgIcon.getImage();
    private Image currentSpriteRestImage = currentSpriteRestIcon.getImage();
    private Image currentSpriteMoving1Image = currentSpriteMoving1Icon.getImage();
    private Image currentSpriteMoving2Image = currentSpriteMoving2Icon.getImage();
    private Image playerCurrentImage = currentSpriteRestImage;
    private Image currentPhaseImage = currentPhaseIcon.getImage();

    // Sound variables
    public Clip collect;
    public Clip stepGrass;
    public Clip backgroundSound;
    public Clip phaseSound;

    // Game variables
    private Rectangle spriteRect;
    private ArrayList<Rectangle> items = new ArrayList<>();
    private ArrayList<Integer> itemImg = new ArrayList<>();
    private ArrayList<Rectangle> objects = new ArrayList<>();
    private ArrayList<Integer> objectImg = new ArrayList<>();
    private int cameraX = 0;
    private int cameraY = 0;
    private int spriteSpeed;
    private int itemsNum = Game.getItems();
    private int objectsNum = random.nextInt(11) + 10;
    private int time = Game.getTime();
    public Timer timer;

    // Animation variables
    private int frames = 0;
    private int currentFrame = 0;
    private int itemFrame = 0;
    private int itemPos = 0;
    private float fade = 255f;
    private float scale = 0f;
    private float alpha = 1f;

    // State variables
    private boolean left, right, up, down, turnleft, turnright;
    private boolean spriteMoving;
    private boolean fadeIn = true;
    public boolean fadeOut = false;
    public boolean paused = false;
    public boolean running = true;
    public boolean winner = false;
    public boolean switchToExit = false;
    public boolean switchToMenu = false;

    // Panels
    private Pause pausePanel;

    // Constructor
    public GamePanel() {
        // --- Panel Properties ---
        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setFocusable(true);
        // Add listeners
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Initialize sounds
        try {
            // Collect sound
            File collectFile = new File(path + "collect.wav");
            AudioInputStream collectAudioStream = AudioSystem.getAudioInputStream(collectFile);
            collect = AudioSystem.getClip();
            collect.open(collectAudioStream);

            // Step sound
            File stepFile = new File(path + "stepGrass.wav");
            AudioInputStream stepAudioStream = AudioSystem.getAudioInputStream(stepFile);
            stepGrass = AudioSystem.getClip();
            stepGrass.open(stepAudioStream);

            // Phase sound
            File phaseFile = new File(path + "phase.wav");
            AudioInputStream phaseAudioStream = AudioSystem.getAudioInputStream(phaseFile);
            phaseSound = AudioSystem.getClip();
            phaseSound.open(phaseAudioStream);
            phaseSound.start();

            // Background sound
            File backgroundFile = new File(path + "background.wav");
            AudioInputStream backgroundAudioStream = AudioSystem.getAudioInputStream(backgroundFile);
            backgroundSound = AudioSystem.getClip();
            backgroundSound.open(backgroundAudioStream);
            backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print the error
            e.printStackTrace();
        }

        // Set world size
        WORLD_WIDTH = Scale.scale(5000);
        WORLD_HEIGHT = Scale.scale(5000);
        // Set sprite speed
        spriteSpeed = Scale.scale(25);
        // Reset score
        Game.score = 0;

        // Create player rectangle in the center of the world
        int playerWidth = Scale.scaleX(currentSpriteRestImage.getWidth(this) / 4);
        int playerHeight = Scale.scaleY(currentSpriteRestImage.getHeight(this) / 4);
        spriteRect = new Rectangle(WORLD_WIDTH / 2 - playerWidth / 2,
                WORLD_HEIGHT / 2 - playerHeight / 2, playerWidth, playerHeight);

        // Initialize pause panel
        pausePanel = new Pause();

        // Create items
        createItems();
        // Create objects
        createObjects();
        // Start timer
        startTimer();
        // Start animation
        startAnimation();
    }

    // Method to start the timer
    private void startTimer() {
        // Create timer
        timer = new Timer();
        // Create timer task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // If the game is not paused and the timer has not ended
                if (!paused) {
                    // Decrease the time
                    time--;
                    if (time <= 0) {
                        // If time runs out, player fails and cannot proceed to next phase
                        winner = false;
                        // Switch to exit panel
                        fadeOut = true;
                        switchToExit = true;
                    }
                }
            }
        };
        // Schedule the task to run every second
        timer.schedule(task, 0, 1000);
    }

    // Method to start the animation
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

    // Method to update the panel
    private void updatePanel() {
        // --- Fade In ---
        if (fadeIn) {
            // Decrease the fade
            fade -= 2.5f;
            if (fade <= 0) {
                fade = 0;
            }
            // Increase the scale
            scale += 0.01f;
            if (scale > 0.5) {
                // Decrease the alpha
                alpha -= 0.01f;
                // If alpha is 0, stop the fade in transition
                if (alpha <= 0) {
                    alpha = 0;
                    fadeIn = false;
                }
            }
        }

        // --- Fade Out ---
        if (fadeOut) {
            // Increase the fade
            fade += 10f;
            // If the fade is greater than 255, make a transition to the menu
            if (fade > 255) {
                fade = 255;
                if (switchToExit || switchToMenu) {
                    // Stop the running loop
                    running = false;
                    // Cancel the timer
                    timer.cancel();
                    // Stop sounds
                    collect.stop();
                    stepGrass.stop();
                    backgroundSound.stop();
                    // Remove this panel
                    Main.frame.remove(this);
                    if (switchToExit) {
                        // Create the exit panel
                        Main.createExitPanel();
                        // Add the exit panel to the frame
                        Main.frame.add(Main.exitPanel);
                        // Revalidate and repaint the frame
                        Main.frame.revalidate();
                        Main.frame.repaint();
                        // Request focus to make keyboard and mouse work
                        Main.exitPanel.requestFocusInWindow();
                    } else if (switchToMenu) {
                        // Create the main panel
                        Main.createMainPanel();
                        // Add the main panel to the frame
                        Main.frame.add(Main.mainPanel);
                        // Revalidate and repaint the frame
                        Main.frame.revalidate();
                        Main.frame.repaint();
                        // Request focus to make keyboard and mouse work
                        Main.mainPanel.requestFocusInWindow();
                    }
                    return;
                }
            }
        }

        // Only update game if not paused
        if (!paused) {
            // --- Player Movement ---
            // Reset the sprite moving flag
            spriteMoving = false;
            // Store old position
            int oldX = spriteRect.x;
            int oldY = spriteRect.y;

            // X movement
            if (left && !right) {
                spriteRect.x -= spriteSpeed;
                spriteMoving = true;
                // Change the facing direction
                turnleft = true;
                turnright = false;
            }
            if (right && !left) {
                spriteRect.x += spriteSpeed;
                spriteMoving = true;
                // Change the facing direction
                turnright = true;
                turnleft = false;
            }

            // Check for collisions with objects on the X axis
            for (Rectangle object : objects) {
                // Check if the player rectangle intersects with the object
                if (spriteRect.intersects(object)) {
                    // Revert the X position
                    spriteRect.x = oldX;
                    break;
                }
            }

            // Y movement
            if (up && !down) {
                spriteRect.y -= spriteSpeed;
                spriteMoving = true;
            }
            if (down && !up) {
                spriteRect.y += spriteSpeed;
                spriteMoving = true;
            }

            // Check for collisions with objects on the Y axis
            for (Rectangle object : objects) {
                // Check if the player rectangle intersects with the object
                if (spriteRect.intersects(object)) {
                    // Revert the Y position
                    spriteRect.y = oldY;
                    break;
                }
            }

            // Some things to do if the sprite is moving
            if (spriteMoving) {
                // Update the frame
                frames += 1;
                if (frames >= 10) {
                    // Change the current frame
                    currentFrame = currentFrame + 1;
                    // If the current frame is greater than 3, reset it to 0
                    if (currentFrame >= 3) {
                        currentFrame = 0;
                    }
                    // Reset the frame
                    frames = 0;
                }
                // Play footstep sound if not already playing
                if (!stepGrass.isRunning()) {
                    stepGrass.stop();
                    stepGrass.setFramePosition(0);
                    stepGrass.start();
                }
            } else {
                // Stop the step sound
                stepGrass.stop();
                // Reset frame if sprite is not moving
                currentFrame = 0;
                frames = 0;
            }

            // Keep player within the world bounds
            spriteRect.x = Math.max(0, Math.min(spriteRect.x, WORLD_WIDTH - spriteRect.width));
            spriteRect.y = Math.max(0, Math.min(spriteRect.y, WORLD_HEIGHT - spriteRect.height));

            // --- Items ---
            // Update the frame
            itemFrame += 1;
            // If the frame is greater than 30, change the offset
            if (itemFrame >= 30) {
                if (itemPos == 0) {
                    itemPos = +Scale.scaleY(5);
                } else {
                    itemPos = 0;
                }
                // Reset the frame
                itemFrame = 0;
            }

            // --- Collisions ---
            // Check for collisions with items
            itemCollisions();

            // --- Camera ---
            // Center camera on player
            cameraX = spriteRect.x + spriteRect.width / 2 - Main.WIDTH / 2;
            cameraY = spriteRect.y + spriteRect.height / 2 - Main.HEIGHT / 2;
            // Keep camera within world bounds
            cameraX = Math.max(0, Math.min(cameraX, WORLD_WIDTH - Main.WIDTH));
            cameraY = Math.max(0, Math.min(cameraY, WORLD_HEIGHT - Main.HEIGHT));
        }
    }

    // Method to create items
    private void createItems() {
        // For loop in range of itemsNum to create items
        for (int i = 0; i < itemsNum; i++) {
            // Random index for the item image between 1 and 10
            int index = random.nextInt(10) + 1;

            // Randomly generate a size for the item
            int size = Scale.scale(random.nextInt(30) + 20);

            // Randomly generate x and y coordinates
            int x = random.nextInt(Scale.scale(50), WORLD_WIDTH - Scale.scale(50) - size);
            int y = random.nextInt(Scale.scale(50), WORLD_HEIGHT - Scale.scale(50) - size);

            // Create a new item rectangle
            Rectangle newItem = new Rectangle(x, y, size, size);
            boolean overlaps = false;

            // Check for overlaps in the items list
            for (Rectangle item : items) {
                // Check if the new item intersects with any existing items
                if (item.intersects(newItem)) {
                    overlaps = true;
                    break;
                }
            }

            // If there is no overlap, add the item to the list
            if (!overlaps) {
                // Add rectangle to the items list
                items.add(newItem);
                // Add the item image index
                itemImg.add(index);
            }
            // If there is an overlap, decrement i to try again
            else {
                i--;
            }
        }
    }

    // Method to create objects on the map
    private void createObjects() {
        // For loop in range of objectsNum to create objects
        for (int i = 0; i < objectsNum; i++) {
            // Random index for the object image between 1 and 3
            int index = random.nextInt(3) + 1;

            // Load image
            ImageIcon icon = new ImageIcon(path + "objects/object" + index + ".png");
            int width = Scale.scaleX(icon.getIconWidth());
            int height = Scale.scaleY(icon.getIconHeight());

            // Random pos
            int x = random.nextInt(Scale.scale(100), WORLD_WIDTH - Scale.scale(100) - width);
            int y = random.nextInt(Scale.scale(100), WORLD_HEIGHT - Scale.scale(100) - height);

            // Create new object rectangle
            Rectangle newObject = new Rectangle(x + width / 3, y, width / 3, height);
            boolean overlaps = false;

            // Check for overlaps with other objects
            int spacing = Scale.scale(750);
            for (Rectangle object : objects) {
                // Calculate the center of the new object
                int centerX1 = x + width / 2;
                int centerY1 = y + height / 2;
                // Calculate the center of the existing object
                int centerX2 = object.x + object.width / 2;
                int centerY2 = object.y + object.height / 2;
                // Calculate the distance between the centers of the two objects
                int distanceX = Math.abs(centerX1 - centerX2);
                int distanceY = Math.abs(centerY1 - centerY2);

                // If the distance is less than the spacing, there is an overlap
                if (distanceX < spacing && distanceY < spacing) {
                    overlaps = true;
                    break;
                }
            }

            // Check for overlaps with items
            for (Rectangle item : items) {
                if (item.intersects(newObject)) {
                    overlaps = true;
                    break;
                }
            }

            // If no overlap, add the object to the list
            if (!overlaps) {
                // Add rectangle to the objects list
                objects.add(newObject);
                // Add the object image index
                objectImg.add(index);
            }
            // If there is an overlap, decrement i to try again
            else {
                i--;
            }
        }
    }

    // Method to check for collisions with items
    private void itemCollisions() {
        // Loop through the items list
        for (int i = 0; i < items.size(); i++) {
            // Get the item rectangle
            Rectangle item = items.get(i);
            // Check if the player rectangle intersects with the item
            if (spriteRect.intersects(item)) {
                // Add score
                Game.score++;
                // Remove item and its corresponding image index
                items.remove(i);
                itemImg.remove(i);
                // Play coin sound
                collect.stop();
                collect.setFramePosition(0);
                collect.start();
                // Check if all items are collected
                if (items.size() == 0) {
                    // Unlock next phase
                    winner = true;
                    Game.unlockPhase();
                    // Fade out and switch to exit panel
                    fadeOut = true;
                    switchToExit = true;
                }
                // Break the loop to prevent crashing
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // --- Graphics2D ---
        Graphics2D g2d = (Graphics2D) g;

        // --- Background ---
        g.drawImage(bgImage, -cameraX, -cameraY, WORLD_WIDTH, WORLD_HEIGHT, this);

        // --- Player Sprite ---
        // Set the current player image based on the current frame
        if (currentFrame == 0) {
            playerCurrentImage = currentSpriteRestImage;
        } else if (currentFrame == 1) {
            playerCurrentImage = currentSpriteMoving1Image;
        } else if (currentFrame == 2) {
            playerCurrentImage = currentSpriteMoving2Image;
        }

        // If the player is moving left, draw the image flipped
        if (turnleft) {
            g.drawImage(playerCurrentImage, spriteRect.x - cameraX + spriteRect.width, spriteRect.y - cameraY,
                    -spriteRect.width,
                    spriteRect.height, this);
        }
        // If the player is moving right, draw the image normally
        else if (turnright) {
            g.drawImage(playerCurrentImage, spriteRect.x - cameraX, spriteRect.y - cameraY, spriteRect.width,
                    spriteRect.height, this);
        } else {
            g.drawImage(playerCurrentImage, spriteRect.x - cameraX, spriteRect.y - cameraY, spriteRect.width,
                    spriteRect.height, this);
        }

        // --- Map Objects ---
        // Draw each object
        for (int i = 0; i < objects.size(); i++) {
            // Get the object rectangle
            Rectangle object = objects.get(i);

            // Get the object icon
            ImageIcon objectIcon = new ImageIcon(path + "objects/object" + objectImg.get(i) + ".png");

            // Get the object image
            Image objectImage = objectIcon.getImage();

            // Get the width and height of the object image
            int width = Scale.scaleX(objectImage.getWidth(this));
            int height = Scale.scaleY(objectImage.getHeight(this));

            // Set the x and y coordinates
            int x = object.x - (width / 3) - cameraX;
            int y = object.y - cameraY;

            // Draw the object image
            g.drawImage(objectImage, x, y, width, height, this);
        }

        // --- Items ---
        // Draw each item
        for (int i = 0; i < items.size(); i++) {
            // Get the item rectangle
            Rectangle item = items.get(i);

            // Get the item icon
            ImageIcon itemIcon = new ImageIcon(path + "items/item" + itemImg.get(i) + ".png");

            // Get the item image
            Image itemImage = itemIcon.getImage();

            // Set the x and y coordinates
            int x = item.x - cameraX;
            int y = item.y - cameraY + itemPos;

            // Yellow glow
            g.setColor(new Color(255, 255, 0, 100));
            g.fillOval(x - Scale.scaleX(5), y - Scale.scaleY(5), item.width + Scale.scaleX(10),
                    item.height + Scale.scaleY(10));

            // White border
            g.setColor(Color.WHITE);
            g.drawOval(x - Scale.scaleX(3), y - Scale.scaleY(3), item.width + Scale.scaleX(6),
                    item.height + Scale.scaleY(6));

            // Draw item
            g.drawImage(itemImage, x, y, item.width, item.height, this);
        }

        // --- Game UI ---
        // Set font
        try {
            g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/Global/mainFont.ttf")).deriveFont(Font.PLAIN,
                    Scale.scale(24)));
        } catch (Exception e) {
            g.setFont(new Font("Segoe UI", Font.BOLD, Scale.scale(24)));
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Items Collected: " + Game.score + "/" + itemsNum, Scale.scaleX(15), Scale.scaleY(40));

        // Draw timer
        if (time <= 10) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString("Time: " + time + "s", Scale.scaleX(15), Scale.scaleY(70));

        // --- Pause Panel ---
        // Use paint component from pause panel to draw
        pausePanel.paintComponent(g);

        // Draw fade overlay
        g.setColor(new Color(0, 0, 0, (int) fade));
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

        // Draw phase image with scale and alpha
        int phaseWidth = (int) (currentPhaseImage.getWidth(this) * scale);
        int phaseHeight = (int) (currentPhaseImage.getHeight(this) * scale);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(currentPhaseImage, Main.WIDTH / 2 - phaseWidth / 2, Main.HEIGHT / 2 - phaseHeight / 2,
                phaseWidth, phaseHeight, this);
    }

    // Key pressed method
    @Override
    public void keyPressed(KeyEvent e) {
        if (pausePanel.visible) {
            pausePanel.keyPressed(e);
        } else {
            // Escape key
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                // Pause the game
                paused = true;
                // Show the pause panel
                pausePanel.visible = true;
                Main.playClickSound();
            }
        }

        // Only allow movement when not paused
        if (!paused) {
            // Left key
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                if (!left) {
                    currentFrame = 1;
                }
                left = true;
            }
            // Right key
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                if (!right) {
                    currentFrame = 1;
                }
                right = true;
            }
            // Up key
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                if (!up) {
                    currentFrame = 1;
                }
                up = true;
            }
            // Down key
            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                if (!down) {
                    currentFrame = 1;
                }
                down = true;
            }
        }
    }

    // Key released method
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Only handle key releases when not paused
        if (!paused) {
            // Left key
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                left = false;
            }
            // Right key
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                right = false;
            }
            // Up key
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                up = false;
            }
            // Down key
            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                down = false;
            }
        }
    }

    // Mouse pressed method
    @Override
    public void mousePressed(MouseEvent e) {
        // If the pause panel is visible
        if (pausePanel.visible) {
            // Call the mouse pressed method from the pause panel
            pausePanel.mousePressed(e);
        }
    }

    // Mouse moved method
    @Override
    public void mouseMoved(MouseEvent e) {
        // If the pause panel is visible
        if (pausePanel.visible) {
            // Call the mouse moved method from the pause panel
            pausePanel.mouseMoved(e);
        }
    }

    // Unused methods
    @Override
    public void keyTyped(KeyEvent e) {
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
    public void mouseDragged(MouseEvent e) {
    }
}