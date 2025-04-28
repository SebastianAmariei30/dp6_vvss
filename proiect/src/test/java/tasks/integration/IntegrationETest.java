package tasks.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationETest {

    ArrayTaskList taskList;
    TasksService tasksService;

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();
        tasksService = new TasksService(taskList);
    }

    @Test
    void testAddRealTaskAndGetObservableList() {
        Task realTask = new Task("Real Task", new Date(1000));
        taskList.add(realTask);

        assertEquals(1, tasksService.getObservableList().size());
        assertEquals(realTask, tasksService.getObservableList().get(0));
    }

    @Test
    void testGetIntervalInHoursWithRealTask() {
        Task realTask = new Task("Real Task", new Date(0), new Date(10000), 3600);
        String interval = tasksService.getIntervalInHours(realTask);
        assertEquals("01:00", interval);
    }
}
