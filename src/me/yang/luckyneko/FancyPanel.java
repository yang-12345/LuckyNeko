package me.yang.luckyneko;

import javax.swing.*;
import java.awt.*;

public class FancyPanel extends JPanel {
    private float fadingProgress;
    protected final MainPanel parent;

    public FancyPanel(MainPanel parent) {
        this.parent = parent;
        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(false);
        this.setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) LuckyNekoUtil.clamp(1.0D - Math.abs(this.fadingProgress - 1.0D), 0.0D, 0.999D)));
        double d = 1.0D - this.fadingProgress;
        if (d >= 0.0D) {
            d = 200.0D * d * d * d;
        } else {
            d = -d;
            d = -200.0D * (d * d * d);
        }
        this.setLocation((int) d, 0);
        super.paint(g);
    }

    public void setFadingProgress(float fadingProgress) {
        this.fadingProgress = fadingProgress;
    }
}
