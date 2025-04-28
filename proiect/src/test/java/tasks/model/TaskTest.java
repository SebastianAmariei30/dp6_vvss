package tasks.model;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testCreateTaskWithoutRepeat() {
        Task task = new Task("Task 1", new Date(1000));
        assertEquals("Task 1", task.getTitle());
        assertEquals(new Date(1000), task.getStartTime());
        assertFalse(task.isRepeated());
    }

    @Test
    void testCreateTaskWithRepeat() {
        Task task = new Task("Task 2", new Date(0), new Date(60000), 10);
        assertTrue(task.isRepeated());
        assertEquals(10, task.getRepeatInterval());
    }

    @Test
    void testSetTitle() {
        Task task = new Task("Old Title", new Date(0));
        task.setTitle("New Title");
        assertEquals("New Title", task.getTitle());
    }

    @Test
    void testSetActive() {
        Task task = new Task("Task Active", new Date(0));
        task.setActive(false);
        assertFalse(task.isActive());
    }
}
