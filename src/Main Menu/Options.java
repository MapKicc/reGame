// Options Panel

// Import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Options panel class
public class Options extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Asset path
    private String path = "assets/MainMenu/Options/";

    // Image icons
    private ImageIcon panelIcon = new ImageIcon(path + "optionsPanel.png");
    private ImageIcon closeBtnIcon = new ImageIcon(path + "cross.png");
    private ImageIcon fscOffBtnIcon = new ImageIcon(path + "fullscreenOffBtn.png");
    private ImageIcon fscOnBtnIcon = new ImageIcon(path + "fullscreenOnBtn.png");

    // Images
    private Image panelImage = panelIcon.getImage();
    private Image closeBtnImage = closeBtnIcon.getImage();
    private Image fscOffBtnImage = fscOffBtnIcon.getImage();
    private Image fscOnBtnImage = fscOnBtnIcon.getImage();
    private Image fscBtn;

    // Rectangles
    private Rectangle panelRect;
    private Rectangle closeBtnRect;
    private Rectangle fscBtnRect;

    // Button hover states
    private boolean closeBtnHover = false;
    private boolean fscBtnHover = false;
    private boolean btnHovered = false;

    // State variables
    public boolean visible = false;

    // Constructor
    public Options() {
        // --- Panel Properties ---
        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setFocusable(true);
        // Add listeners
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Initialize rectangles
        panelRect = new Rectangle();
        closeBtnRect = new Rectangle();
        fscBtnRect = new Rectangle();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If panel is not visible, return
        if (!visible)
            return;

        // --- Background Overlay ---
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

        // --- Panel Background ---
        // Panel properties
        int panelWidth = Scale.scaleX(1100);
        int panelHeight = Scale.scaleY(850);
        // Set new bounds for panel
        panelRect.setBounds((Main.WIDTH - panelWidth) / 2, (Main.HEIGHT - panelHeight) / 2, panelWidth, panelHeight);
        // Draw panel
        g.drawImage(panelImage, panelRect.x, panelRect.y, panelRect.width, panelRect.height, this);

        // --- Buttons ---
        // Set new bounds for buttons
        closeBtnRect.setBounds(panelRect.x + panelRect.width - Scale.scaleX(30), panelRect.y + Scale.scaleY(150),
                Scale.scaleX(55), Scale.scaleY(55));
        fscBtnRect.setBounds(panelRect.x + (panelRect.width - Scale.scaleX(340)) / 2, panelRect.y + Scale.scaleY(250),
                Scale.scaleX(340), Scale.scaleY(145));

        // Hover effect properties
        int hoverPos = Scale.scale(5);
        int hoverDim = hoverPos * 2;

        // Draw close button
        if (closeBtnHover) {
            g.drawImage(closeBtnImage, closeBtnRect.x - hoverPos, closeBtnRect.y - hoverPos,
                    closeBtnRect.width + hoverDim, closeBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(closeBtnImage, closeBtnRect.x, closeBtnRect.y, closeBtnRect.width, closeBtnRect.height, this);
        }

        // Check fullscreen state
        if (Scale.fullscreen) {
            fscBtn = fscOnBtnImage;
        } else {
            fscBtn = fscOffBtnImage;
        }
        // Draw fullscreen toggle button
        if (fscBtnHover) {
            g.drawImage(fscBtn, fscBtnRect.x - hoverPos, fscBtnRect.y - hoverPos, fscBtnRect.width + hoverDim,
                    fscBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(fscBtn, fscBtnRect.x, fscBtnRect.y, fscBtnRect.width, fscBtnRect.height, this);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // If panel is not visible, return
        if (!visible)
            return;

        // Close button
        if (closeBtnRect.contains(e.getPoint())) {
            Main.playClickSound();
            visible = false;
        }
        // Fullscreen toggle button
        if (fscBtnRect.contains(e.getPoint())) {
            Main.playClickSound();
            Scale.toggleFullscreen();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // If panel is not visible, return
        if (!visible)
            return;

        // Store the old hovered state
        boolean btnOldHovered = btnHovered;

        // Check if the mouse is hovering over any button
        closeBtnHover = closeBtnRect.contains(e.getPoint());
        fscBtnHover = fscBtnRect.contains(e.getPoint());

        // If any button is hovered, set btnHovered to true
        btnHovered = closeBtnHover || fscBtnHover;

        // If any button is hovered and the old hovered state is false, play hover sound
        if (btnHovered && !btnOldHovered) {
            Main.playHoverSound();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // If panel is not visible, return
        if (!visible)
            return;

        // Escape key
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Main.playClickSound();
            visible = false;
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