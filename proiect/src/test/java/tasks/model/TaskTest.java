package tasks.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Task focusing on task creation and time validation
 */
@DisplayName("Task Creation and Validation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskTest {

    private static final SimpleDateFormat dateFormat = Task.getDateFormat();
    private Task task;
    private Date baseDate;

    @BeforeEach
    void setUp() throws ParseException {
        baseDate = dateFormat.parse("2023-02-12 10:10");
        task = new Task("Test Task", baseDate);
    }

    @AfterEach
    void tearDown() {
        task = null;
    }

    /**
     * Test for basic task creation
     */
    @Test
    @Order(1)
    @DisplayName("Basic task creation with valid parameters")
    void testBasicTaskCreation() {
        // Assert
        assertEquals("Test Task", task.getTitle(), "Task title should match");
        assertEquals(baseDate, task.getTime(), "Task time should match");
        assertFalse(task.isActive(), "New task should not be active by default");
        assertFalse(task.isRepeated(), "New task should not be repeated by default");
    }

    /**
     * Test for verifying formatted date representation
     */
    @Test
    @Order(2)
    @DisplayName("Task date formatting")
    void testTaskDateFormatting() throws ParseException {
        // Arrange - using the task from setup
        String expectedFormatted = "2023-02-12 10:10";

        // Assert
        assertEquals(expectedFormatted, task.getFormattedDateStart(), "Formatted date should match expected string");
    }

    /**
     * Testing task creation with invalid parameters - negative time
     */
    @Test
    @Order(3)
    @Tag("ECP")
    @DisplayName("Task creation with negative time throws exception")
    void testTaskCreationWithNegativeTime() {
        // Arrange
        Date negativeDate = new Date(-10000); // Negative timestamp

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Task("Test Task", negativeDate),
                "Creating task with negative time should throw IllegalArgumentException");
    }

    /**
     * Testing repeated task creation with valid parameters
     */
    @Test
    @Order(4)
    @Tag("ECP")
    @DisplayName("Repeated task creation with valid parameters")
    void testRepeatedTaskCreation() throws ParseException {
        // Arrange
        Date startDate = dateFormat.parse("2023-02-12 10:00");
        Date endDate = dateFormat.parse("2023-02-15 10:00");
        int interval = 3600; // 1 hour in seconds

        // Act
        Task repeatedTask = new Task("Repeated Task", startDate, endDate, interval);

        // Assert
        assertTrue(repeatedTask.isRepeated(), "Task should be repeated");
        assertEquals(startDate, repeatedTask.getStartTime(), "Start time should match");
        assertEquals(endDate, repeatedTask.getEndTime(), "End time should match");
        assertEquals(interval, repeatedTask.getRepeatInterval(), "Interval should match");
    }

    /**
     * Testing repeated task creation with invalid interval (less than 1)
     */
    @Test
    @Order(5)
    @Tag("ECP")
    @DisplayName("Repeated task creation with invalid interval throws exception")
    void testRepeatedTaskCreationWithInvalidInterval() throws ParseException {
        // Arrange
        Date startDate = dateFormat.parse("2023-02-12 10:00");
        Date endDate = dateFormat.parse("2023-02-15 10:00");
        int invalidInterval = 0; // Invalid interval

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Task("Repeated Task", startDate, endDate, invalidInterval),
                "Creating repeated task with interval < 1 should throw IllegalArgumentException");
    }

    /**
     * Parameterized test for next time calculation
     */
    @ParameterizedTest
    @Order(6)
    @MethodSource("provideNextTimeTestData")
    @DisplayName("Next time calculation for tasks")
    void testNextTimeAfter(Date currentTime, Date expectedNext, boolean repeated, boolean active) throws ParseException {
        // Arrange
        if (repeated) {
            Date startTime = dateFormat.parse("2023-02-12 10:00");
            Date endTime = dateFormat.parse("2023-02-15 10:00");
            task = new Task("Repeated Task", startTime, endTime, 3600); // 1 hour interval
        }

        task.setActive(active);

        // Act
        Date actualNext = task.nextTimeAfter(currentTime);

        // Assert
        assertEquals(expectedNext, actualNext,
                "Next time calculation should match expected result");
    }

    /**
     * Testing boundary values for task time
     */
    @ParameterizedTest
    @Order(7)
    @ValueSource(strings = {"2023-02-12 00:00", "2023-02-12 23:59"})
    @Tag("BVA")
    @DisplayName("Task creation with boundary time values")
    void testTaskCreationWithBoundaryTimeValues(String dateString) throws ParseException {
        // Arrange
        Date boundaryDate = dateFormat.parse(dateString);

        // Act
        Task boundaryTask = new Task("Boundary Task", boundaryDate);

        // Assert
        assertEquals(boundaryDate, boundaryTask.getTime(),
                "Task time should match the boundary value");
    }

    /**
     * Testing repeated task with boundary interval values
     */
    @Test
    @Order(8)
    @Tag("BVA")
    @DisplayName("Repeated task with minimum valid interval")
    void testRepeatedTaskWithMinimumInterval() throws ParseException {
        // Arrange
        Date startDate = dateFormat.parse("2023-02-12 10:00");
        Date endDate = dateFormat.parse("2023-02-15 10:00");
        int minInterval = 1; // Minimum valid interval

        // Act
        Task repeatedTask = new Task("Repeated Task", startDate, endDate, minInterval);

        // Assert
        assertEquals(minInterval, repeatedTask.getRepeatInterval(),
                "Interval should be set to minimum valid value");
    }

    /**
     * Test data provider for nextTimeAfter tests
     */
    private static Stream<Arguments> provideNextTimeTestData() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return Stream.of(
                // For non-repeated tasks (current time, expected next time, is repeated, is active)
                Arguments.of(df.parse("2023-02-12 09:00"), df.parse("2023-02-12 10:10"), false, true),  // Before task time
                Arguments.of(df.parse("2023-02-12 10:10"), null, false, true),                          // At task time
                Arguments.of(df.parse("2023-02-12 11:00"), null, false, true),                          // After task time
                Arguments.of(df.parse("2023-02-12 09:00"), null, false, false)                          // Inactive task
        );
    }

    /**
     * Example of a test with timeout
     */
    @Test
    @Timeout(1)
    @Order(9)
    @DisplayName("Task operation should complete within timeout")
    void testTaskOperationTimeout() {
        // This test will fail if it takes longer than 1 second
        // Just a simple operation that should be quick
        task.setTitle("Updated Title");
        assertEquals("Updated Title", task.getTitle(), "Title should be updated");
    }

    /**
     * Failing test example - commented out to prevent actual failure
     */
    /*
    @Test
    @Order(10)
    @DisplayName("Example of a failing test")
    void testFailingExample() {
        fail("This test is deliberately set to fail");
    }
    */
}