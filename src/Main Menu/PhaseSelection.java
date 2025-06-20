// Phase Selection Panel

// Import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Phase selection panel class
public class PhaseSelection extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Asset path
    private String path = "assets/Global/selection/";

    // Image icons
    private ImageIcon panelIcon = new ImageIcon(path + "panel.png");
    private ImageIcon selectBtnIcon = new ImageIcon(path + "selectBtn.png");
    private ImageIcon selectedBtnIcon = new ImageIcon(path + "selectedBtn.png");
    private ImageIcon closeBtnIcon = new ImageIcon(path + "cross.png");
    private ImageIcon phase1Icon = new ImageIcon("assets/Global/phases/phase1.png");
    private ImageIcon phase2Icon = new ImageIcon("assets/Global/phases/phase2.png");
    private ImageIcon phase3Icon = new ImageIcon("assets/Global/phases/phase3.png");

    // Images
    private Image panelImage = panelIcon.getImage();
    private Image selectBtnImage = selectBtnIcon.getImage();
    private Image selectedBtnImage = selectedBtnIcon.getImage();
    private Image closeBtnImage = closeBtnIcon.getImage();
    private Image phase1Image = phase1Icon.getImage();
    private Image phase2Image = phase2Icon.getImage();
    private Image phase3Image = phase3Icon.getImage();

    // Rectangles
    private Rectangle panelRect;
    private Rectangle closeBtnRect;
    private Rectangle phase1BtnRect;
    private Rectangle phase2BtnRect;
    private Rectangle phase3BtnRect;

    // Button hover states
    private boolean closeBtnHover = false;
    private boolean phase1BtnHover = false;
    private boolean phase2BtnHover = false;
    private boolean phase3BtnHover = false;
    private boolean btnHovered = false;

    // State variables
    public boolean visible = false;

    // Constructor
    public PhaseSelection() {
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
        phase1BtnRect = new Rectangle();
        phase2BtnRect = new Rectangle();
        phase3BtnRect = new Rectangle();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If panel is not visible, return
        if (!visible)
            return;

        // --- Graphics2D ---
        Graphics2D g2d = (Graphics2D) g;

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
        // Hover effect properties
        int hoverPos = Scale.scale(5);
        int hoverDim = hoverPos * 2;

        // Set new bounds for the button
        closeBtnRect.setBounds(panelRect.x + panelRect.width - Scale.scaleX(70), panelRect.y + Scale.scaleY(15),
                Scale.scaleX(55), Scale.scaleY(55));

        // Set new bounds for the button
        if (closeBtnHover) {
            g.drawImage(closeBtnImage, closeBtnRect.x - hoverPos, closeBtnRect.y - hoverPos,
                    closeBtnRect.width + hoverDim, closeBtnRect.height + hoverDim, this);
        } else {
            g.drawImage(closeBtnImage, closeBtnRect.x, closeBtnRect.y,
                    closeBtnRect.width, closeBtnRect.height, this);
        }

        // --- Phases and Buttons ---
        // Properties
        int btnWidth = Scale.scaleX(200);
        int btnHeight = Scale.scaleY(80);
        int spacing = Scale.scaleX(80);
        int startX = panelRect.x + (panelRect.width - (3 * btnWidth + 2 * spacing)) / 2;
        int totalHeight = Scale.scaleX(95) + Scale.scaleY(20) + btnHeight;
        int phaseImgY = panelRect.y + (panelRect.height - totalHeight) / 2;
        int phaseBtnY = phaseImgY + Scale.scaleX(95) + Scale.scaleY(20);

        // Set new bounds for buttons
        phase1BtnRect.setBounds(startX, phaseBtnY, btnWidth, btnHeight);
        phase2BtnRect.setBounds(startX + btnWidth + spacing, phaseBtnY, btnWidth, btnHeight);
        phase3BtnRect.setBounds(startX + 2 * (btnWidth + spacing), phaseBtnY, btnWidth, btnHeight);

        // --- Phase 1 ---
        drawPhase(g, g2d, phase1Image, phase1BtnRect, 1, phaseImgY, phase1BtnHover, Game.phase1, hoverPos, hoverDim);

        // --- Phase 2 ---
        drawPhase(g, g2d, phase2Image, phase2BtnRect, 2, phaseImgY, phase2BtnHover, Game.phase2, hoverPos, hoverDim);

        // --- Phase 3 ---
        drawPhase(g, g2d, phase3Image, phase3BtnRect, 3, phaseImgY, phase3BtnHover, Game.phase3, hoverPos, hoverDim);
    }

    // Method to draw a phase and its button
    private void drawPhase(Graphics g, Graphics2D g2d, Image phaseImage, Rectangle btnRect, int phaseNum, int phaseImgY,
            boolean hovered, boolean unlocked, int hoverPos, int hoverDim) {
        // Properties
        int size = Scale.scaleX(80);
        Image btnImage;

        // Check if this phase is selected and set corresponding image and size
        if (Game.currentPhase == phaseNum) {
            btnImage = selectedBtnImage;
            size = Scale.scaleX(95);
        } else {
            btnImage = selectBtnImage;
        }

        // Check if phase is unlocked and draw accordingly
        if (unlocked) {
            // Draw phase image
            g.drawImage(phaseImage, btnRect.x + (btnRect.width - size) / 2, phaseImgY, size, size, this);

            // Draw phase button
            if (hovered) {
                g.drawImage(btnImage, btnRect.x - hoverPos, btnRect.y - hoverPos,
                        btnRect.width + hoverDim, btnRect.height + hoverDim, this);
            } else {
                g.drawImage(btnImage, btnRect.x, btnRect.y, btnRect.width, btnRect.height, this);
            }
        } else {
            // Change alpha of the phase image
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.drawImage(phaseImage, btnRect.x + (btnRect.width - size) / 2, phaseImgY, size, size, this);
            // Change alpha of the select button
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2d.drawImage(selectBtnImage, btnRect.x, btnRect.y, btnRect.width, btnRect.height, this);
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
        // Phase 1 button
        if (phase1BtnRect.contains(e.getPoint())) {
            if (Game.phase1) {
                Main.playClickSound();
                // Set current phase to 1
                Game.currentPhase = 1;
            }
        }
        // Phase 2 button
        if (phase2BtnRect.contains(e.getPoint())) {
            if (Game.phase2) {
                Main.playClickSound();
                // Set current phase to 2
                Game.currentPhase = 2;
            }
        }
        // Phase 3 button
        if (phase3BtnRect.contains(e.getPoint())) {
            if (Game.phase3) {
                Main.playClickSound();
                // Set current phase to 3
                Game.currentPhase = 3;
            }
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
        phase1BtnHover = phase1BtnRect.contains(e.getPoint());
        phase2BtnHover = phase2BtnRect.contains(e.getPoint()) && Game.phase2;
        phase3BtnHover = phase3BtnRect.contains(e.getPoint()) && Game.phase3;

        // If any button is hovered, set btnHovered to true
        btnHovered = closeBtnHover || phase1BtnHover || phase2BtnHover || phase3BtnHover;

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