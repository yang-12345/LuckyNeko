package me.yang.luckyneko;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A button with more beautiful looks.
 */
public class FancyButton extends JButton implements TranslatedTextHolder {
    private boolean hovered;
    private double animationProgress;
    protected long lastRenderingTime = System.nanoTime();
    private TranslatedText text;

    public FancyButton(String text) {
        this(text, null);
    }

    public FancyButton(TranslatedText text) {
        this(text.getString(), null);
        this.text = text;
    }

    public FancyButton(String text, Icon icon) {
        super(text, icon);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setFont(LuckyNekoUtil.TEXT_FONT);
        this.setForeground(LuckyNekoUtil.TEXT_COLOR);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!hovered) {
                    hovered = true;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hovered) {
                    hovered = false;
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        long l = System.nanoTime();
        this.animationProgress = LuckyNekoUtil.clamp(this.animationProgress
                        + (this.hovered ? 1.0D : -1.0D) * (l - this.lastRenderingTime) / 200000000.0D,
                0.0D, 1.0D);
        this.lastRenderingTime = l;
        super.paint(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int i = (int) LuckyNekoUtil.lerp(224, 208, this.getAnimationProgress());
        g.setColor(new Color(i, i, i));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        super.paintComponent(g);
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public double getAnimationProgress() {
        return this.animationProgress;
    }

    @Override
    public void reload(Language language) {
        if (this.text != null) {
            this.setText(this.text.getString(language));
        }
    }
}
