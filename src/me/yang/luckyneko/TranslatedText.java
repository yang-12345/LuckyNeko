package me.yang.luckyneko;

import java.util.function.Supplier;

public record TranslatedText(String key, Supplier<?>... args) {
    public String getString() {
        return this.getString(Language.getLanguage());
    }

    public String getString(Language language) {
        Object[] objects = new Object[this.args.length];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = this.args[i].get();
        }
        return language.get(this.key, objects);
    }
}
