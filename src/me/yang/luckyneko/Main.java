package me.yang.luckyneko;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final boolean FPS_SHOWN = true;

    public static void main(String[] args) {
        Language.load();
        try {
            File file = new File("options.txt");
            FileInputStream fin = new FileInputStream(file);
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(fin));
            String str = buffReader.readLine();
            Language.setLanguage(str != null ? str : "en_us");
        } catch (IOException ignored) {
            Language.setLanguage("en_us");
        }
        JFrame frame = new JFrame("Lucky Neko");
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        MainPanel panel = new MainPanel();
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }
        LuckyNekoUtil.getPath("/assets/icon.png").ifPresent(url -> {
            try {
                Image icon = ImageIO.read(url);
                frame.setIconImage(icon);
            } catch (IOException ignored) {
            }
        });
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.onResized();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    LotteryManager<String> lotteryManager = panel.lotteryPanel.getLotteryManager();
                    FileWriter writer = new FileWriter("namelist.txt");
                    for (String str : lotteryManager.getEntries()) {
                        writer.write(str + '\n');
                    }
                    writer.close();
                    FileWriter writer2 = new FileWriter("options.txt");
                    writer2.write(Language.getLanguage().getId());
                    writer2.close();
                } catch (IOException ignored) {
                }
            }
        });
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(panel::repaint, 0, 16, TimeUnit.MILLISECONDS);
    }
}