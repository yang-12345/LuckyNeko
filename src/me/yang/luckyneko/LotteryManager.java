package me.yang.luckyneko;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class LotteryManager<T> {
    private final List<T> entries = new ArrayList<>();
    private final Random random = new Random();

    public LotteryManager() {
    }

    public List<T> getEntries() {
        return this.entries;
    }

    public void addEntry(T entry) {
        this.entries.add(entry);
    }

    public void removeEntry(T entry) {
        this.entries.remove(entry);
    }

    public T randomlyPick() {
        return this.isEmpty() ? null : this.entries.get(random.nextInt(this.entries.size()));
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public int size() {
        return this.entries.size();
    }

    public static <T> LotteryManager<T> fromFile(File file, LotteryCodec<T> codec) throws IOException {
        LotteryManager<T> lotteryManager = new LotteryManager<>();
        FileInputStream fin = new FileInputStream(file);
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(fin));
        String str;
        while ((str = buffReader.readLine()) != null) {
            if (!str.isBlank()) {
                lotteryManager.addEntry(codec.get(str));
            }
        }
        return lotteryManager;
    }

    public interface LotteryCodec<T> {
        T get(String line);
    }
}
