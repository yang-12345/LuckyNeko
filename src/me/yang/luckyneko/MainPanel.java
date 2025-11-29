package me.yang.luckyneko;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private FancyPanel currentPanel;
    private FancyPanel lastPanel;
    public final WelcomePanel welcomePanel;
    public final LotteryPanel lotteryPanel;
    private long panelFadingTimer = System.nanoTime();
    private long lastRenderingTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;

    public MainPanel() {
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.welcomePanel = new WelcomePanel(this);
        this.welcomePanel.setSize(1200, 800);
        this.lotteryPanel = new LotteryPanel(this);
        this.lotteryPanel.setSize(1200, 800);
        this.currentPanel = this.welcomePanel;
        this.add(this.welcomePanel);
        this.add(this.lotteryPanel);
    }

    @Override
    public void paint(Graphics g) {
        this.panelFadingTimer++;
        super.paint(g);
        float f = Math.min(1.0F, (System.nanoTime() - this.panelFadingTimer) / 0.6E9F);
        this.currentPanel.setFadingProgress(f);
        if (this.lastPanel != null) {
            this.lastPanel.setFadingProgress(Math.min(2.0F, 1.0F + 1.2F * f));
            if (f == 1.0f) {
                this.lastPanel.setVisible(false);
                this.lastPanel = null;
            }
        }

        if (Main.FPS_SHOWN) {
            this.frames++;
            if (System.nanoTime() - this.lastRenderingTime >= 1000000000L) {
                this.fps = this.frames;
                this.frames = 0;
                this.lastRenderingTime = System.nanoTime();
            }
            g.setFont(LuckyNekoUtil.TEXT_FONT.deriveFont(Font.PLAIN, 12.0F));
            g.setColor(LuckyNekoUtil.TEXT_COLOR);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawString("FPS: " + this.fps, 5, 15);
        }
    }

    public void switchPanel(FancyPanel panel) {
        if (this.lastPanel == panel) {
            return;
        }
        this.panelFadingTimer = System.nanoTime();
        this.lastPanel = this.currentPanel;
        this.currentPanel = panel;
        this.currentPanel.setVisible(true);
    }

    public void onResized() {
        for (Component component : this.getComponents()) {
            if (component instanceof FancyPanel) {
                component.setSize(this.getSize());
            }
        }
    }
}
