package structures;

import metrics.Metrics;

public class MinHeap {
    private int[] elements = new int[10];
    private int size;
    private final Metrics metrics = new Metrics();

    public Metrics getMetrics() {
        return metrics;
    }

    public int size() {
        return size;
    }

    public void insert(int value) {
        if (size == elements.length) {
            int[] larger = new int[elements.length * 2];
            for (int i = 0; i < size; i++) {
                larger[i] = elements[i];
                metrics.steps++;
                metrics.moves++;
            }
            elements = larger;
        }
        elements[size] = value;
        size++;
        int index = size - 1;
        while (index > 0) {
            int parent = (index - 1) / 2;
            metrics.steps += 2;
            metrics.comparisons++;
            if (elements[parent] <= elements[index]) {
                break;
            }
            swap(parent, index);
            index = parent;
        }
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        return elements[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int minimum = elements[0];
        metrics.steps++;
        size--;
        if (size > 0) {
            elements[0] = elements[size];
            metrics.steps++;
            metrics.moves++;
            bubbleDown();
        }
        return minimum;
    }

    private void bubbleDown() {
        int index = 0;
        while (2 * index + 1 < size) {
            // Both child subtrees are heaps; only index may violate the order.
            int left = 2 * index + 1;
            int right = left + 1;
            int smallerChild = left;
            if (right < size) {
                metrics.steps += 2;
                metrics.comparisons++;
                if (elements[right] < elements[left]) {
                    smallerChild = right;
                }
            }
            metrics.steps += 2;
            metrics.comparisons++;
            if (elements[index] <= elements[smallerChild]) {
                break;
            }
            swap(index, smallerChild);
            index = smallerChild;
        }
    }

    private void swap(int first, int second) {
        int temporary = elements[first];
        elements[first] = elements[second];
        elements[second] = temporary;
        metrics.steps += 2;
        metrics.moves += 2;
    }

    // A simple diagnostic for checking the heap property in tests.
    public boolean hasHeapProperty() {
        for (int child = 1; child < size; child++) {
            int parent = (child - 1) / 2;
            if (elements[parent] > elements[child]) {
                return false;
            }
        }
        return true;
    }
}
