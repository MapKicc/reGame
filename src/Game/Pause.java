// Pause Panel

// Import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Pause panel class
public class Pause extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Asset path
    private String path = "assets/Game/Pause/";

    // Image icons
    private ImageIcon resumeBtnIcon = new ImageIcon(path + "resumeBtn.png");
    private ImageIcon resumeBtnHoveredIcon = new ImageIcon(path + "resumeBtnHovered.png");
    private ImageIcon quitBtnIcon = new ImageIcon(path + "quitBtn.png");
    private ImageIcon quitBtnHoveredIcon = new ImageIcon(path + "quitBtnHovered.png");
    private ImageIcon pausedIcon = new ImageIcon(path + "paused.png");

    // Images
    private Image resumeBtnImage = resumeBtnIcon.getImage();
    private Image resumeBtnHoveredImage = resumeBtnHoveredIcon.getImage();
    private Image quitBtnImage = quitBtnIcon.getImage();
    private Image quitBtnHoveredImage = quitBtnHoveredIcon.getImage();
    private Image pausedImage = pausedIcon.getImage();

    // Rectangles
    private Rectangle resumeBtnRect;
    private Rectangle quitBtnRect;

    // Button hover states
    private boolean resumeBtnHover = false;
    private boolean quitBtnHover = false;
    private boolean btnHovered = false;

    // Panel visible
    public boolean visible = false;

    // Constructor
    public Pause() {
        // --- Panel Properties ---
        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setFocusable(true);
        // Add listeners
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Initialize rectangles
        resumeBtnRect = new Rectangle();
        quitBtnRect = new Rectangle();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If panel is not visible, return
        if (!visible)
            return;

        // --- Buttons ---
        // Button properties
        int buttonWidth = Scale.scaleX(resumeBtnImage.getWidth(this) / 2);
        int buttonHeight = Scale.scaleY(resumeBtnImage.getHeight(this) / 2);
        int buttonSpacing = Scale.scaleY(25);
        int menuCenterX = Main.WIDTH / 2 - buttonWidth / 2;
        int menuStartY = Main.HEIGHT / 2 - (2 * buttonHeight + buttonSpacing) / 2;

        // Set new bounds for buttons
        resumeBtnRect.setBounds(menuCenterX, menuStartY, buttonWidth, buttonHeight);
        quitBtnRect.setBounds(menuCenterX, menuStartY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);

        // --- Background Overlay ---
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

        // --- Paused ---
        int pausedWidth = (int) (Main.WIDTH / 4);
        int pausedHeight = (int) (pausedWidth * 540 / 860);
        int pausedX = (Main.WIDTH - pausedWidth) / 2;
        int pausedY = Main.HEIGHT / 2 - Scale.scaleY(500);
        g.drawImage(pausedImage, pausedX, pausedY, pausedWidth, pausedHeight, this);

        // --- Buttons ---
        // Hover effect properties
        int hoverPos = Scale.scale(5);
        int hoverDim = hoverPos * 2;

        // Draw Resume button
        if (resumeBtnHover) {
            g.drawImage(resumeBtnHoveredImage, resumeBtnRect.x - hoverPos, resumeBtnRect.y - hoverPos,
                    resumeBtnRect.width + hoverDim, resumeBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(resumeBtnImage, resumeBtnRect.x, resumeBtnRect.y, resumeBtnRect.width, resumeBtnRect.height,
                    this);
        }

        // Draw Quit button
        if (quitBtnHover) {
            g.drawImage(quitBtnHoveredImage, quitBtnRect.x - hoverPos, quitBtnRect.y - hoverPos,
                    quitBtnRect.width + hoverDim, quitBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(quitBtnImage, quitBtnRect.x, quitBtnRect.y, quitBtnRect.width, quitBtnRect.height, this);
        }
    }

    // Mouse pressed method
    @Override
    public void mousePressed(MouseEvent e) {
        // If panel is not visible, return
        if (!visible)
            return;

        // Resume button
        if (resumeBtnRect.contains(e.getPoint())) {
            // Resume game
            Main.gamePanel.paused = false;
            // Hide panel
            visible = false;
            Main.playClickSound();
        }
        // Quit button
        if (quitBtnRect.contains(e.getPoint())) {
            // Fade out and switch to menu
            Main.gamePanel.fadeOut = true;
            Main.gamePanel.switchToMenu = true;
        }
    }

    // Mouse moved method
    @Override
    public void mouseMoved(MouseEvent e) {
        // If panel is not visible, return
        if (!visible)
            return;

        // Store the old hovered state
        boolean btnOldHovered = btnHovered;

        // Check if the mouse is hovering over any button
        resumeBtnHover = resumeBtnRect.contains(e.getPoint());
        quitBtnHover = quitBtnRect.contains(e.getPoint());

        // If any button is hovered, set btnHovered to true
        btnHovered = resumeBtnHover || quitBtnHover;

        // If any button is hovered and the old hovered state is false, play hover sound
        if (btnHovered && !btnOldHovered) {
            Main.playHoverSound();
        }
    }

    // Key pressed method
    @Override
    public void keyPressed(KeyEvent e) {
        // If panel is not visible, return
        if (!visible)
            return;

        // Escape key
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // Resume game
            Main.gamePanel.paused = false;
            // Hide panel
            visible = false;
            Main.playClickSound();
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
