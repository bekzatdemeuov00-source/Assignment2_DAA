import org.junit.jupiter.api.Test;
import structures.MinHeap;
import java.util.PriorityQueue;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {
    @Test
    void emptyAndOneElement() {
        MinHeap heap = new MinHeap();
        assertEquals(0, heap.size());
        assertTrue(heap.hasHeapProperty());
        try {
            heap.peekMin();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // Correct exception.
        }
        try {
            heap.extractMin();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // Correct exception.
        }
        heap.insert(7);
        assertEquals(7, heap.peekMin());
        assertEquals(1, heap.size());
        assertTrue(heap.hasHeapProperty());
        assertEquals(7, heap.extractMin());
        assertEquals(0, heap.size());
        assertTrue(heap.hasHeapProperty());
        try {
            heap.extractMin();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // Correct exception.
        }
    }

    @Test
    void duplicatesNegativeAndExtremeValues() {
        MinHeap heap = new MinHeap();
        int[] values = {7, -2, 7, Integer.MAX_VALUE, Integer.MIN_VALUE, -2};
        for (int value : values) {
            heap.insert(value);
            assertTrue(heap.hasHeapProperty());
        }
        int[] expected = {Integer.MIN_VALUE, -2, -2, 7, 7, Integer.MAX_VALUE};
        for (int value : expected) {
            assertEquals(value, heap.extractMin());
            assertTrue(heap.hasHeapProperty());
        }
    }

    @Test
    void randomOperationsMatchPriorityQueue() {
        MinHeap heap = new MinHeap();
        PriorityQueue<Integer> expected = new PriorityQueue<>();
        Random random = new Random(42);
        for (int operation = 0; operation < 5000; operation++) {
            if (expected.isEmpty() || random.nextBoolean()) {
                int value = random.nextInt();
                heap.insert(value);
                expected.add(value);
            } else {
                assertEquals(expected.remove().intValue(), heap.extractMin());
            }
            assertEquals(expected.size(), heap.size());
            assertTrue(heap.hasHeapProperty());
            if (!expected.isEmpty()) {
                assertEquals(expected.peek().intValue(), heap.peekMin());
            }
        }
        while (!expected.isEmpty()) {
            assertEquals(expected.remove().intValue(), heap.extractMin());
            assertTrue(heap.hasHeapProperty());
        }
    }

    @Test
    void extractionIsNonDecreasingAfterManyInsertions() {
        MinHeap heap = new MinHeap();
        Random random = new Random(42);
        for (int i = 0; i < 10000; i++) {
            heap.insert(random.nextInt());
            assertTrue(heap.hasHeapProperty());
        }
        int previous = Integer.MIN_VALUE;
        while (heap.size() > 0) {
            int current = heap.extractMin();
            assertTrue(previous <= current);
            assertTrue(heap.hasHeapProperty());
            previous = current;
        }
    }
}
