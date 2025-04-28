package tasks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksServiceTest {

    ArrayTaskList taskList;
    TasksService tasksService;

    @BeforeEach
    void setUp() {
        taskList = mock(ArrayTaskList.class); // aici facem mock, nu spy
        tasksService = new TasksService(taskList);
    }

    @Test
    void testGetObservableList() {
        when(taskList.getAll()).thenReturn(java.util.Collections.singletonList(new Task("Mocked Task", new Date(0))));

        assertEquals(1, tasksService.getObservableList().size());
        assertEquals("Mocked Task", tasksService.getObservableList().get(0).getTitle());
    }

    @Test
    void testGetIntervalInHours() {
        Task mockTask = mock(Task.class);
        when(mockTask.getRepeatInterval()).thenReturn(3600); // 3600 sec = 1 oră

        String interval = tasksService.getIntervalInHours(mockTask);
        assertEquals("01:00", interval);
    }
}
