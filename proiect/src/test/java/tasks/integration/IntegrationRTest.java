package tasks.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IntegrationRTest {

    ArrayTaskList taskList;
    TasksService tasksService;

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();
        tasksService = new TasksService(taskList);
    }

    @Test
    void testAddTaskAndGetObservableList() {
        Task mockTask = mock(Task.class);
        when(mockTask.getTitle()).thenReturn("Mock Task");
        when(mockTask.getStartTime()).thenReturn(new Date(1000));

        taskList.add(mockTask);

        assertEquals(1, tasksService.getObservableList().size());
        assertEquals(mockTask, tasksService.getObservableList().get(0));
    }

    @Test
    void testGetIntervalInHoursWithMockedTask() {
        Task mockTask = mock(Task.class);
        when(mockTask.getRepeatInterval()).thenReturn(3600);

        String interval = tasksService.getIntervalInHours(mockTask);
        assertEquals("01:00", interval);
    }
}
