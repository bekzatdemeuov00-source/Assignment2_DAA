package benchmark;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import metrics.Metrics;
import structures.DynamicArray;
import structures.MyLinkedList;
import structures.MinHeap;

public class Benchmark {
    public static void main(String[] args) throws IOException {
        new File("results/plots").mkdirs();
        int[] sizes = {100, 1000, 10000, 100000};
        try (PrintWriter writer = new PrintWriter("results/results.csv", "UTF-8")) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
            for (int n : sizes) {
                Random random = new Random(42);
                int[] values = new int[n];
                for (int i = 0; i < n; i++) {
                    values[i] = random.nextInt(n);
                }
                randomAccess(writer, values);
                search(writer, values);
                insertAndRemove(writer, values, "head");
                insertAndRemove(writer, values, "middle");
                priorityProcessing(writer, n);
                writer.flush();
                System.out.println("Finished n=" + n);
            }
            if (writer.checkError()) {
                throw new IOException("Could not write results.csv");
            }
        }
    }

    private static void randomAccess(PrintWriter writer, int[] values) {
        int n = values.length;
        Random random = new Random(42);
        int[] indexes = new int[10000];
        long expectedSum = 0;
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = random.nextInt(n);
            expectedSum += values[indexes[i]];
        }
        long[] arrayTimes = new long[5];
        long[] listTimes = new long[5];
        for (int run = 0; run < 7; run++) {
            DynamicArray array = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) {
                array.add(values[i]);
                list.add(values[i]);
            }
            array.getMetrics().reset();
            list.getMetrics().reset();

            long arraySum = 0;
            long start = System.nanoTime();
            for (int index : indexes) {
                arraySum += array.get(index);
            }
            long arrayTime = System.nanoTime() - start;

            long listSum = 0;
            start = System.nanoTime();
            for (int index : indexes) {
                listSum += list.get(index);
            }
            long listTime = System.nanoTime() - start;
            if (arraySum != expectedSum || listSum != expectedSum) {
                throw new IllegalStateException("Incorrect get results");
            }
            // Runs 0 and 1 are warm-ups. Runs 2 through 6 are measured.
            if (run >= 2) {
                arrayTimes[run - 2] = arrayTime;
                listTimes[run - 2] = listTime;
            }
            if (run == 6) {
                save(writer, "W1", "-", "DynamicArray", n, arrayTimes, array.getMetrics());
                save(writer, "W1", "-", "MyLinkedList", n, listTimes, list.getMetrics());
            }
        }
    }

    private static void search(PrintWriter writer, int[] values) {
        int n = values.length;
        Random random = new Random(42);
        int[] queries = new int[1000];
        for (int i = 0; i < queries.length; i++) {
            if (i < 500) {
                queries[i] = values[random.nextInt(n)];
            } else {
                queries[i] = n + random.nextInt(n);
            }
        }
        long[] arrayTimes = new long[5];
        long[] listTimes = new long[5];
        for (int run = 0; run < 7; run++) {
            DynamicArray array = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) {
                array.add(values[i]);
                list.add(values[i]);
            }
            array.getMetrics().reset();
            list.getMetrics().reset();

            int arrayFound = 0;
            long start = System.nanoTime();
            for (int query : queries) {
                if (array.contains(query)) {
                    arrayFound++;
                }
            }
            long arrayTime = System.nanoTime() - start;

            int listFound = 0;
            start = System.nanoTime();
            for (int query : queries) {
                if (list.contains(query)) {
                    listFound++;
                }
            }
            long listTime = System.nanoTime() - start;
            if (arrayFound != 500 || listFound != 500) {
                throw new IllegalStateException("Incorrect contains results");
            }
            if (run >= 2) {
                arrayTimes[run - 2] = arrayTime;
                listTimes[run - 2] = listTime;
            }
            if (run == 6) {
                save(writer, "W2", "-", "DynamicArray", n, arrayTimes, array.getMetrics());
                save(writer, "W2", "-", "MyLinkedList", n, listTimes, list.getMetrics());
            }
        }
    }

    private static void insertAndRemove(PrintWriter writer, int[] values, String variant) {
        int n = values.length;
        int index = 0;
        if (variant.equals("middle")) {
            index = n / 2;
        }
        long[] arrayTimes = new long[5];
        long[] listTimes = new long[5];
        for (int run = 0; run < 7; run++) {
            DynamicArray array = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) {
                array.add(values[i]);
                list.add(values[i]);
            }
            array.getMetrics().reset();
            list.getMetrics().reset();

            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                array.add(index, i);
            }
            int arraySum = 0;
            for (int i = 0; i < 1000; i++) {
                arraySum += array.remove(index);
            }
            long arrayTime = System.nanoTime() - start;

            start = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                list.add(index, i);
            }
            int listSum = 0;
            for (int i = 0; i < 1000; i++) {
                listSum += list.remove(index);
            }
            long listTime = System.nanoTime() - start;
            if (arraySum != 499500 || listSum != 499500
                    || array.size() != n || list.size() != n) {
                throw new IllegalStateException("Incorrect insert/remove results");
            }
            if (run >= 2) {
                arrayTimes[run - 2] = arrayTime;
                listTimes[run - 2] = listTime;
            }
            if (run == 6) {
                save(writer, "W3", variant, "DynamicArray", n, arrayTimes, array.getMetrics());
                save(writer, "W3", variant, "MyLinkedList", n, listTimes, list.getMetrics());
            }
        }
    }

    private static void priorityProcessing(PrintWriter writer, int n) {
        Random random = new Random(42);
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = random.nextInt();
        }
        long[] times = new long[5];
        for (int run = 0; run < 7; run++) {
            MinHeap heap = new MinHeap();
            int[] extracted = new int[n];
            long start = System.nanoTime();
            for (int value : values) {
                heap.insert(value);
            }
            for (int i = 0; i < n; i++) {
                extracted[i] = heap.extractMin();
            }
            long elapsed = System.nanoTime() - start;
            for (int i = 1; i < n; i++) {
                if (extracted[i] < extracted[i - 1]) {
                    throw new IllegalStateException("Heap output is not sorted");
                }
            }
            if (run >= 2) {
                times[run - 2] = elapsed;
            }
            if (run == 6) {
                save(writer, "W4", "-", "MinHeap", n, times, heap.getMetrics());
            }
        }
    }

    private static void save(PrintWriter writer, String workload, String variant,
                             String structure, int n, long[] times, Metrics metrics) {
        Arrays.sort(times);
        double milliseconds = times[2] / 1_000_000.0;
        writer.println(workload + "," + variant + "," + structure + "," + n + ","
                + milliseconds + "," + metrics.steps + "," + metrics.moves
                + "," + metrics.comparisons);
    }
}
