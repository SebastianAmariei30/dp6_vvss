package tasks.model;

import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BBTTest {
    private ArrayTaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();
        Task task = new Task("Test data", new Date(), new Date(), 1);
        taskList.add(task);
    }

    @DisplayName("Save task with valid short description")
    //ECP
    @Test
    void saveWithValidDescription() {
        Task task = new Task("Do it", new Date(), new Date(), 1);

        Assertions.assertDoesNotThrow(() -> taskList.add(task));
        assertEquals(2, taskList.size());
    }

    @Test
    void saveWithInvalidDescription() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task(null, new Date(), new Date(), 1);
            taskList.add(task);
        });
        assertEquals(1, taskList.size());
    }


    @Test
    void saveWithValidDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 1, 10, 0, 0);  // luni sunt zero-based: 0 = ianuarie
        Date startDate = calendar.getTime();

        calendar.set(2024, Calendar.JANUARY, 1, 23, 59, 59); // sfârșitul aceleiași zile
        Date endDate = calendar.getTime();

        Task task = new Task("Do it", startDate, endDate, 1);

        Assertions.assertDoesNotThrow(() -> taskList.add(task));
        assertEquals(2, taskList.size());
    }

    @Test
    void saveWithInvalidDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1960, Calendar.JANUARY, 1, 0, 0, 0);  // luni sunt zero-based: 0 = ianuarie
        Date startDate = calendar.getTime();

        calendar.set(1960, Calendar.JANUARY, 1, 23, 59, 59); // sfârșitul aceleiași zile
        Date endDate = calendar.getTime();


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("Do it", startDate, endDate, 1);
            taskList.add(task);
        });
        assertEquals(1, taskList.size());
    }

    //BVA
    @RepeatedTest(3)
    @DisplayName("Test repetat pentru adăugare task valid")
    void saveWithValidDescriptionBva() {
        Task task = new Task("D", new Date(), new Date(), 1);

        Assertions.assertDoesNotThrow(() -> taskList.add(task));
        assertEquals(2, taskList.size());
    }
    @Tag("bva")
    @Test
    void saveWithInvalidDescriptionLowerBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("", new Date(), new Date(), 1);
            taskList.add(task);
        });
        assertEquals(1, taskList.size());
    }
    @Nested
    class Bva2{
        @Test
    void saveWithValidDateBva() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 1, 8, 0, 0);  // luni sunt zero-based: 0 = ianuarie
        Date startDate = calendar.getTime();

        calendar.set(2024, Calendar.JANUARY, 1, 23, 59, 59); // sfârșitul aceleiași zile
        Date endDate = calendar.getTime();

        Task task = new Task("Do it", startDate, endDate, 1);

        Assertions.assertDoesNotThrow(() -> taskList.add(task));
        assertEquals(2, taskList.size());
    }

        @Test
        void saveWithInvalidDateLowerBound() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1960, Calendar.JANUARY, 1, 7, 0, 0);  // luni sunt zero-based: 0 = ianuarie
            Date startDate = calendar.getTime();

            calendar.set(1960, Calendar.JANUARY, 1, 23, 59, 59); // sfârșitul aceleiași zile
            Date endDate = calendar.getTime();


            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Task task = new Task("Do it", startDate, endDate, 1);
                taskList.add(task);
            });
            assertEquals(1, taskList.size());
        }
    }

}

class SpyTest{
    ArrayTaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = spy(new ArrayTaskList()); // folosim spy ca să putem verifica metode
    }

    @Test
    void testAddTask() {
        Task task = new Task("New Task", new Date(0));
        taskList.add(task);

        verify(taskList, times(1)).add(task);
        assertEquals(1, taskList.size());
        assertEquals(task, taskList.getTask(0));
    }

    @Test
    void testRemoveTask() {
        Task task = new Task("Task to Remove", new Date(0));
        taskList.add(task);
        boolean removed = taskList.remove(task);

        verify(taskList, times(1)).remove(task);
        assertTrue(removed);
        assertEquals(0, taskList.size());
    }
}