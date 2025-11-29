package me.yang.luckyneko;

import java.util.ArrayList;
import java.util.List;

public class LotteryList<T> {
    private final List<T> positiveElements;
    private final List<T> negativeElements;
    private T zeroElement;
    private final LotteryManager<T> manager;
    private int size = 0;

    public LotteryList(LotteryManager<T> manager) {
        this.manager = manager;
        this.positiveElements = new ArrayListX<>();
        this.negativeElements = new ArrayListX<>();
        this.zeroElement = this.manager.randomlyPick();
    }

    public int size() {
        return this.size;
    }

    public void setSize(int size) {
        if (this.size > size) {
            this.negativeElements.subList(size, this.size).clear();
            this.positiveElements.subList(size, this.size).clear();
        } else if (this.size < size) {
            for (int i = this.size; i < size; i++) {
                this.positiveElements.set(i, this.manager.randomlyPick());
                this.negativeElements.set(i, this.manager.randomlyPick());
            }
        }
        this.size = size;
    }

    public void shiftTowardPositive(int shift) {
        for (int n = 0; n < shift; n++) {
            for (int i = this.size - 1; i > 0; i--) {
                this.positiveElements.set(i, this.positiveElements.get(i - 1));
            }
            this.positiveElements.set(0, this.zeroElement);
            this.zeroElement = this.negativeElements.getFirst();
            for (int i = 0; i < this.size - 1; i++) {
                this.negativeElements.set(i, this.negativeElements.get(i + 1));
            }
            this.negativeElements.set(this.size - 1, this.manager.randomlyPick());
        }
    }

    public void shiftTowardNegative(int shift) {
        for (int n = 0; n < shift; n++) {
            for (int i = this.size - 1; i > 0; i--) {
                this.negativeElements.set(i, this.negativeElements.get(i - 1));
            }
            this.negativeElements.set(0, this.zeroElement);
            this.zeroElement = this.positiveElements.getFirst();
            for (int i = 0; i < this.size - 1; i++) {
                this.positiveElements.set(i, this.positiveElements.get(i + 1));
            }
            this.positiveElements.set(this.size - 1, this.manager.randomlyPick());
        }
    }

    public void shift(int shift) {
        if (shift > 0) {
            this.shiftTowardPositive(shift);
        } else if (shift < 0) {
            this.shiftTowardNegative(-shift);
        }
    }

    public T get(int index) {
        if (index > 0) {
            return this.positiveElements.get(index - 1);
        } else if (index < 0) {
            return this.negativeElements.get(-1 - index);
        } else {
            return this.zeroElement;
        }
    }

    public void clear() {
        this.zeroElement = this.manager.randomlyPick();
        this.size = 0;
        this.positiveElements.clear();
        this.negativeElements.clear();
    }

    static class ArrayListX<T> extends ArrayList<T> {
        @Override
        public T set(int index, T element) {
            if (index == this.size()) {
                this.addLast(null);
            }
            return super.set(index, element);
        }
    }
}
