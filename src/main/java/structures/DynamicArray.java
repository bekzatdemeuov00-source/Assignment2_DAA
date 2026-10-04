package structures;

import metrics.Metrics;

public class DynamicArray {
    private int[] elements = new int[10];
    private int size;
    private final Metrics metrics = new Metrics();

    public Metrics getMetrics() {
        return metrics;
    }

    public int size() {
        return size;
    }

    public void add(int value) {
        add(size, value);
    }

    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (size == elements.length) {
            int[] larger = new int[elements.length * 2];
            for (int i = 0; i < size; i++) {
                larger[i] = elements[i];
                metrics.steps++;
                metrics.moves++;
            }
            elements = larger;
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
            metrics.steps++;
            metrics.moves++;
        }
        elements[index] = value;
        size++;
    }

    public int remove(int index) {
        checkIndex(index);
        int removed = elements[index];
        metrics.steps++;
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
            metrics.steps++;
            metrics.moves++;
        }
        size--;
        return removed;
    }

    public int get(int index) {
        checkIndex(index);
        metrics.steps++;
        return elements[index];
    }

    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            // All elements before i have already been checked.
            metrics.steps++;
            metrics.comparisons++;
            if (elements[i] == value) {
                return true;
            }
        }
        return false;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }
}
