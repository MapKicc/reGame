// Sprite Selection Panel

// Import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Sprite selection panel class
public class SpriteSelection extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Asset path
    private String path = "assets/Global/selection/";

    // Image icons
    private ImageIcon panelIcon = new ImageIcon(path + "panel.png");
    private ImageIcon selectBtnIcon = new ImageIcon(path + "selectBtn.png");
    private ImageIcon selectedBtnIcon = new ImageIcon(path + "selectedBtn.png");
    private ImageIcon closeBtnIcon = new ImageIcon(path + "cross.png");
    private ImageIcon sprite1Icon = new ImageIcon("assets/Global/sprites/sprite1/sprite1Rest.png");

    // Images
    private Image panelImage = panelIcon.getImage();
    private Image selectBtnImage = selectBtnIcon.getImage();
    private Image selectedBtnImage = selectedBtnIcon.getImage();
    private Image closeBtnImage = closeBtnIcon.getImage();
    private Image sprite1Image = sprite1Icon.getImage();
    private Image sprite1BtnImage;

    // Rectangles
    private Rectangle panelRect;
    private Rectangle closeBtnRect;
    private Rectangle sprite1BtnRect;

    // Button hover states
    private boolean closeBtnHover = false;
    private boolean sprite1BtnHover = false;
    private boolean btnHovered = false;

    // State variables
    public boolean visible = false;

    // Constructor
    public SpriteSelection() {
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
        sprite1BtnRect = new Rectangle();
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
        int panelWidth = Scale.scaleX(950);
        int panelHeight = Scale.scaleY(540);
        // Set new bounds for panel
        panelRect.setBounds((Main.WIDTH - panelWidth) / 2, (Main.HEIGHT - panelHeight) / 2, panelWidth, panelHeight);
        // Draw panel
        g.drawImage(panelImage, panelRect.x, panelRect.y, panelRect.width, panelRect.height, this);

        // --- Close Button ---
        // Set new bounds for buttons
        closeBtnRect.setBounds(panelRect.x + panelRect.width - Scale.scaleX(70), panelRect.y + Scale.scaleY(15),
                Scale.scaleX(55), Scale.scaleY(55));

        // Hover effect properties
        int hoverPos = Scale.scale(5);
        int hoverDim = hoverPos * 2;

        // Draw close button
        if (closeBtnHover) {
            g.drawImage(closeBtnImage, closeBtnRect.x - hoverPos, closeBtnRect.y - hoverPos,
                    closeBtnRect.width + hoverDim, closeBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(closeBtnImage, closeBtnRect.x, closeBtnRect.y,
                    closeBtnRect.width, closeBtnRect.height, this);
        }

        // --- Sprite 1 ---
        // Properties
        int btnWidth = Scale.scaleX(200);
        int btnHeight = Scale.scaleY(80);
        int spriteWidth = Scale.scaleX((int) (sprite1Image.getWidth(this) / 3.5));
        int spriteHeight = Scale.scaleY((int) (sprite1Image.getHeight(this) / 3.5));
        int startX = panelRect.x + (panelRect.width - btnWidth) / 2;
        int totalHeight = spriteHeight + Scale.scaleY(20) + btnHeight;
        int spriteImgY = panelRect.y + (panelRect.height - totalHeight) / 2;
        int spriteBtnY = spriteImgY + spriteHeight + Scale.scaleY(20);

        // Set new bounds for the button
        sprite1BtnRect.setBounds(startX, spriteBtnY, btnWidth, btnHeight);

        // Check if sprite1 is selected and set corresponding image and size
        if (Game.currentSprite.equals("sprite1")) {
            sprite1BtnImage = selectedBtnImage;
            spriteWidth = spriteWidth + Scale.scaleX(15);
            spriteHeight = spriteHeight + Scale.scaleY(15);
        } else {
            sprite1BtnImage = selectBtnImage;
        }

        // Draw sprite image
        int spriteImgX = sprite1BtnRect.x + (sprite1BtnRect.width - spriteWidth) / 2;
        g.drawImage(sprite1Image, spriteImgX, spriteImgY, spriteWidth, spriteHeight, this);

        // Draw sprite button
        if (sprite1BtnHover) {
            g.drawImage(sprite1BtnImage, sprite1BtnRect.x - hoverPos, sprite1BtnRect.y - hoverPos,
                    sprite1BtnRect.width + hoverDim, sprite1BtnRect.height + hoverDim, this);
        } else {
            g.drawImage(sprite1BtnImage, sprite1BtnRect.x, sprite1BtnRect.y,
                    sprite1BtnRect.width, sprite1BtnRect.height, this);
        }
    }

    // Mouse pressed method
    @Override
    public void mousePressed(MouseEvent e) {
        // If panel is not visible, return
        if (!visible)
            return;

        // Close button
        if (closeBtnRect.contains(e.getPoint())) {
            Main.playClickSound();
            // Make panel invisible
            visible = false;
        }
        // Sprite 1 button
        if (sprite1BtnRect.contains(e.getPoint())) {
            Main.playClickSound();
            // Set current sprite to sprite1
            Game.currentSprite = "sprite1";
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
        closeBtnHover = closeBtnRect.contains(e.getPoint());
        sprite1BtnHover = sprite1BtnRect.contains(e.getPoint());

        // If any button is hovered, set btnHovered to true
        btnHovered = closeBtnHover || sprite1BtnHover;

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
            Main.playClickSound();
            // Make panel invisible
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