import metrics.Metrics;
import org.junit.jupiter.api.Test;
import structures.MyLinkedList;
import java.util.LinkedList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {
    @Test
    void emptyAndInvalidIndexes() {
        MyLinkedList list = new MyLinkedList();
        assertEquals(0, list.size());
        assertFalse(list.contains(5));
        try {
            list.get(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            list.remove(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            list.add(-1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            list.add(1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        list.add(5);
        try {
            list.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            list.get(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            list.remove(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            list.remove(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
        try {
            list.add(2, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Correct exception.
        }
    }

    @Test
    void oneElementDuplicatesAndEnds() {
        MyLinkedList list = new MyLinkedList();
        list.add(0, 7);
        assertEquals(7, list.get(0));
        assertTrue(list.contains(7));
        assertEquals(7, list.remove(0));
        list.add(7);
        list.add(0, 3);
        list.add(list.size(), 7);
        assertEquals(3, list.remove(0));
        assertEquals(7, list.remove(list.size() - 1));
        list.add(9);
        assertEquals(9, list.get(list.size() - 1));
        assertEquals(7, list.remove(0));
        assertFalse(list.contains(7));
        assertEquals(9, list.remove(0));
        assertEquals(0, list.size());
        list.add(11);
        assertEquals(11, list.get(0));
    }

    @Test
    void randomOperationsMatchLinkedList() {
        MyLinkedList list = new MyLinkedList();
        LinkedList<Integer> expected = new LinkedList<>();
        Random random = new Random(42);
        for (int operation = 0; operation < 2000; operation++) {
            if (expected.isEmpty() || random.nextBoolean()) {
                int index = random.nextInt(expected.size() + 1);
                int value = random.nextInt(50) - 25;
                list.add(index, value);
                expected.add(index, value);
            } else {
                int index = random.nextInt(expected.size());
                assertEquals(expected.remove(index).intValue(), list.remove(index));
            }
            assertEquals(expected.size(), list.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i).intValue(), list.get(i));
            }
            int query = random.nextInt(100) - 50;
            assertEquals(expected.contains(query), list.contains(query));
        }
    }

    @Test
    void traversalAndLinkMetrics() {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        Metrics metrics = list.getMetrics();
        metrics.reset();
        assertEquals(3, list.get(2));
        assertEquals(2, metrics.steps);
        assertEquals(0, metrics.moves);
        metrics.reset();
        assertFalse(list.contains(4));
        assertEquals(3, metrics.steps);
        assertEquals(3, metrics.comparisons);
        metrics.reset();
        list.add(0, 0);
        assertEquals(2, metrics.moves);
        assertEquals(0, metrics.steps);
    }
}
