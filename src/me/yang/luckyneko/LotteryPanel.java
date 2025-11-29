package me.yang.luckyneko;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class LotteryPanel extends FancyPanel {
    private static final int ENTRY_SPACE = 80;
    private static final LotteryManager.LotteryCodec<String> LOTTERY_CODEC = String::trim;
    private LotteryManager<String> lotteryManager;
    private LotteryList<String> lotteryList;
    private final FancyLabel hintLabel1;
    private final FancyLabel hintLabel2;
    private long lastRenderingTime = System.nanoTime();
    private double scrollY = 0;
    private double scrollVelocity = 98.0D;
    private int lastY = -1;
    private double nameFieldLength = 0.0D;
    private boolean everDragged = false;

    public LotteryPanel(MainPanel parent) {
        super(parent);
        try {
            this.lotteryManager = LotteryManager.fromFile(new File("namelist.txt"), LOTTERY_CODEC);
        } catch (IOException e) {
            this.lotteryManager = new LotteryManager<>();
        }
        this.lotteryList = new LotteryList<>(this.lotteryManager);
        this.hintLabel1 = new FancyLabel("");
        this.hintLabel1.setFont(this.hintLabel1.getFont().deriveFont(30.0F));
        this.hintLabel1.setBounds(30, 40, 500, 40);
        this.hintLabel2 = new FancyLabel("");
        this.hintLabel2.setFont(this.hintLabel2.getFont().deriveFont(18.0F));
        this.hintLabel2.setBounds(30, 85, 500, 40);
        this.add(hintLabel1);
        this.add(hintLabel2);
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastY != -1) {
                    scrollVelocity += 5.0D * (lastY - e.getY());
                }
                lastY = e.getY();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastY = -1;
            }
        });

        FancyButton button = new FancyButton(new TranslatedText("panel.lottery.import"));
        button.setBounds(30, 140, 152, 35);
        button.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(new File("."));
            chooser.setFileFilter(new FileNameExtensionFilter("TXT File", "txt"));
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    lotteryManager = LotteryManager.fromFile(chooser.getSelectedFile(), LOTTERY_CODEC);
                    lotteryList = new LotteryList<>(lotteryManager);
                } catch (IOException ignored) {
                }
            }
        });
        this.add(button);
    }

    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        int h = this.getHeight();
        int w = this.getWidth();
        boolean bl = this.lotteryManager.isEmpty();

        long l = System.nanoTime();
        double d = (l - lastRenderingTime) / 1.0E9D;
        g.setFont(LuckyNekoUtil.TEXT_FONT.deriveFont(22.0F));
        if (bl) {
            this.hintLabel1.setText(new TranslatedText("panel.lottery.hint.empty.1"));
            this.hintLabel2.setText(new TranslatedText("panel.lottery.hint.empty.2"));
        } else {
            this.hintLabel1.setText(new TranslatedText("panel.lottery.hint.ready.1"));
            this.hintLabel2.setText(new TranslatedText("panel.lottery.hint.ready.2", this.lotteryManager::size));
            double e = g.getFontMetrics().stringWidth(lotteryList.get(0)) + 0.5D - this.nameFieldLength;
            if (Math.abs(e) >= 1.0D) {
                this.nameFieldLength += 3.0D * d * LuckyNekoUtil.clamp(5.0D * Math.abs(e), 10.0D, 1000.0D) * Math.signum(e);
            }
            this.nameFieldLength = LuckyNekoUtil.clamp(this.nameFieldLength, 0.0D, w);
            g.setColor(Color.LIGHT_GRAY);
            int i = (int) (nameFieldLength + 30.0D);
            g.fillRect(w / 2 - i / 2, h / 2 - 35, i, 40);
        }
        g.setColor(LuckyNekoUtil.TEXT_COLOR);
        if (!bl) {
            /* random list loop */
            int i = (int) Math.floor(scrollY / ENTRY_SPACE);
            scrollY -= i * ENTRY_SPACE;
            lotteryList.shift(-i);
            lotteryList.setSize(3 + h / (2 * ENTRY_SPACE));
            for (int j = 2 - lotteryList.size(); j < lotteryList.size() - 1; j++) {
                String str = lotteryList.get(j);
                g.drawString(str, -g.getFontMetrics().stringWidth(str) / 2 + w / 2, (int) (30 + h / 2.0D + j * ENTRY_SPACE - scrollY));
            }
        }
        scrollY += scrollVelocity * d;
        scrollVelocity = 0.98D * (scrollVelocity + (everDragged ? 0.0D : 2.0D));
        double scrollSpeed = Math.abs(scrollVelocity);
        if (scrollSpeed > 200.0D) {
            this.everDragged = true;
        }
        if (everDragged && scrollSpeed < 100.0D) {
            scrollVelocity += -3.0D * Math.sin(Math.PI * 2.0D * (scrollY / ENTRY_SPACE - 0.5D));
        }
        if (scrollSpeed < 10.0D && Math.abs(scrollY / ENTRY_SPACE - 0.5D) < 0.01D) {
            scrollVelocity = 0.0D;
        }
        //scrollSpeed = Math.signum(scrollSpeed) * Math.abs(Math.max(scrollSpeed - Math.signum(scrollSpeed) * Math.min(Math.abs(scrollSpeed) + 0.0D, 500.0D) * d, 0.0D));
        lastRenderingTime = l;
    }

    public LotteryManager<String> getLotteryManager() {
        return lotteryManager;
    }
}
