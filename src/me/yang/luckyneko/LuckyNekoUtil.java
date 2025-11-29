package me.yang.luckyneko;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class LuckyNekoUtil {
    public static final Color TEXT_COLOR = new Color(65, 74, 94);
    public static final Font TEXT_FONT = new Font("Microsoft YaHei UI", Font.BOLD, 14);

    public static double clamp(double value, double min, double max) {
        if (Double.isNaN(min) || Double.isNaN(max)) {
            return Double.NaN;
        }
        if (min == max) {
            return min;
        }
        if (min > max) {
            return clamp(value, max, min);
        }
        return Math.min(max, Math.max(min, value));
    }

    public static double lerp(double start, double end, double delta) {
        return clamp(start + delta * (end - start), start, end);
    }

    public static Optional<URL> getPath(String path) {
        return Optional.ofNullable(Main.class.getResource(path));
    }

    public static ImageIcon getIcon(String path) {
        Optional<URL> optional = getPath(path);
        return optional.map(url -> {
            try {
                return new ImageIcon(ImageIO.read(url));
            } catch (IOException e) {
                return null;
            }
        }).orElse(null);
    }
}
