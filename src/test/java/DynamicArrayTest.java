import metrics.Metrics;
import org.junit.jupiter.api.Test;
import structures.DynamicArray;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {
    @Test
    void emptyAndInvalidIndexes() {
        DynamicArray array = new DynamicArray();
        assertEquals(0, array.size());
        assertFalse(array.contains(5));
        try {
            array.get(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            array.remove(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            array.add(-1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            array.add(1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        array.add(5);
        try {
            array.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            array.get(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            array.remove(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            array.remove(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            array.add(2, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
    }

    @Test
    void oneElementDuplicatesAndEnds() {
        DynamicArray array = new DynamicArray();
        array.add(0, 7);
        assertEquals(7, array.get(0));
        assertTrue(array.contains(7));
        assertEquals(7, array.remove(0));
        assertEquals(0, array.size());
        array.add(7);
        array.add(0, 3);
        array.add(array.size(), 7);
        assertEquals(3, array.remove(0));
        assertEquals(7, array.remove(array.size() - 1));
        assertTrue(array.contains(7));
        assertEquals(7, array.remove(0));
        assertFalse(array.contains(7));
    }

    @Test
    void randomOperationsMatchArrayList() {
        DynamicArray array = new DynamicArray();
        ArrayList<Integer> expected = new ArrayList<>();
        Random random = new Random(42);
        for (int operation = 0; operation < 2000; operation++) {
            if (expected.isEmpty() || random.nextBoolean()) {
                int index = random.nextInt(expected.size() + 1);
                int value = random.nextInt(50) - 25;
                array.add(index, value);
                expected.add(index, value);
            } else {
                int index = random.nextInt(expected.size());
                assertEquals(expected.remove(index).intValue(), array.remove(index));
            }
            assertEquals(expected.size(), array.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i).intValue(), array.get(i));
            }
            int query = random.nextInt(100) - 50;
            assertEquals(expected.contains(query), array.contains(query));
        }
    }

    @Test
    void resizingAndMetrics() {
        DynamicArray array = new DynamicArray();
        for (int i = 0; i < 1000; i++) {
            array.add(i);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(i, array.get(i));
        }
        Metrics metrics = array.getMetrics();
        metrics.reset();
        assertFalse(array.contains(-1));
        assertEquals(1000, metrics.steps);
        assertEquals(1000, metrics.comparisons);
        assertEquals(0, metrics.moves);
        metrics.reset();
        array.add(0, -1);
        assertEquals(1000, metrics.steps);
        assertEquals(1000, metrics.moves);
    }
}
