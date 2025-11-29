package me.yang.luckyneko;

import java.awt.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class WelcomePanel extends FancyPanel {
    private final List<FancyLabel> labels;

    public WelcomePanel(MainPanel parent) {
        super(parent);
        this.labels = new ArrayList<>();

        int i = ZonedDateTime.now().getHour();
        String str;
        if (i <= 4 || i == 23) {
            str = "panel.welcome.title.night";
        } else if (i <= 6) {
            str = "panel.welcome.title.dawn";
        } else if (i <= 9) {
            str = "panel.welcome.title.early_morning";
        } else if (i <= 11) {
            str = "panel.welcome.title.morning";
        } else if (i <= 14) {
            str = "panel.welcome.title.noon";
        } else if (i <= 17) {
            str = "panel.welcome.title.afternoon";
        } else {
            str = "panel.welcome.title.evening";
        }
        FancyLabel welcomeLabel = new FancyLabel(new TranslatedText(str));
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(30.0F));
        welcomeLabel.setBounds(30, 20, 500, 40);
        this.labels.add(welcomeLabel);

        FancyLabel descriptionLabel = new FancyLabel(new TranslatedText("panel.welcome.description"));
        descriptionLabel.setFont(descriptionLabel.getFont().deriveFont(18.0F));
        descriptionLabel.setBounds(30, 75, 500, 26);
        this.labels.add(descriptionLabel);

        FancyButton button = new FancyButton(new TranslatedText("panel.welcome.start"));
        button.setBounds(30, 120, 122, 35);
        button.addActionListener(e -> this.parent.switchPanel(this.parent.lotteryPanel));
        this.add(button);

        this.labels.forEach(this::add);
    }

    @Override
    protected void paintComponent(Graphics g) {
        long l = System.nanoTime();
//        for (FancyLabel label : this.labels) {
//            double d = 1.0D - LuckyNekoUtil.clamp(-0.005D * label.getY() + (l - this.firstRenderingTime) / 1.0E9D, 0.0D, 1.0D);
//            label.setLocation((int) (30.0D + 150.0D * d * d * d), label.getY());
//            int i = (int) Math.min(255.0D, 280.0D * (1.0D - d));
//            Color color = label.getForeground();
//            label.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), i));
//        }
        super.paintComponent(g);
    }
}
