package me.yang.luckyneko;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Language {
    private static final Map<String, Language> LANGUAGES = new HashMap<>();
    private static Language language;
    private final String id;
    private final Map<String, String> entries;

    private Language(String id, Map<String, String> entries) {
        this.id = id;
        this.entries = entries;
    }

    public static void setLanguage(String str) {
        if (LANGUAGES.containsKey(str)) {
            language = LANGUAGES.get(str);
        } else {
            language = LANGUAGES.get("en_us");
        }
    }

    public static Language getLanguage() {
        return language;
    }

    public String getId() {
        return this.id;
    }

    public String get(String key, Object... args) {
        if (language == null || !this.entries.containsKey(key)) {
            return key;
        }
        try {
            return String.format(this.entries.get(key), args);
        } catch (IllegalFormatException e) {
            return key;
        }
    }

    public static void load() {
        LuckyNekoUtil.getPath("/assets/lang/").ifPresent(url -> {
            try {
                File file = new File(url.toURI());
                File[] files = file.listFiles((dir, name) -> name.endsWith(".txt"));
                Map<String, Language> map = new HashMap<>();
                if (files != null) {
                    for (File f : files) {
                        Map<String, String> map2 = new HashMap<>();
                        try (FileInputStream fileInputStream = new FileInputStream(f)) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                            String str;
                            while ((str = bufferedReader.readLine()) != null) {
                                int i = str.indexOf('=');
                                if (i != -1) {
                                    map2.put(str.substring(0, i).trim(), str.substring(i + 1));
                                }
                            }
                        }
                        String s = f.getName();
                        String str = s.substring(0, s.lastIndexOf('.'));
                        map.put(str, new Language(str, map2));
                    }
                }
                LANGUAGES.clear();
                LANGUAGES.putAll(map);
            } catch (IOException | URISyntaxException ignored) {
            }
        });
    }
}
