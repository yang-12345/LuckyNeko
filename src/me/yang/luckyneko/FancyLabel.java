package me.yang.luckyneko;

import javax.swing.*;

/**
 * A label with more beautiful looks.
 */
public class FancyLabel extends JLabel implements TranslatedTextHolder {
    private TranslatedText text;

    public FancyLabel(TranslatedText text) {
        this(text.getString());
        this.text = text;
    }

    public FancyLabel(String text) {
        super(text);
        this.setFont(LuckyNekoUtil.TEXT_FONT);
        this.setForeground(LuckyNekoUtil.TEXT_COLOR);
    }

    public void setText(TranslatedText text) {
        this.text = text;
        this.setText(this.text.getString());
    }

    @Override
    public void reload(Language language) {
        if (this.text != null) {
            this.setText(this.text.getString(language));
        }
    }
}
