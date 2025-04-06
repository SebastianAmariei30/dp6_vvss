package tasks.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TasksService focusing on the parseFromStringToSeconds method
 * which converts time strings in hh:MM format to seconds
 */
@DisplayName("TasksService Time Parsing Tests")
class TasksServiceTest {

    private TasksService tasksService;

    @BeforeEach
    void setUp() {
        // We can pass null since we're only testing the parseFromStringToSeconds method
        // which doesn't use the tasks list
        tasksService = new TasksService(null);
    }

    /**
     * Tests for valid time string inputs using ECP
     * Valid inputs should be in format hh:MM where:
     * - hh is between 0 and 23
     * - MM is between 00 and 59
     */
    @ParameterizedTest(name = "Valid time {0} should convert to {1} seconds")
    @CsvSource({
            "00:00, 0",         // EC1: Lower bound
            "12:30, 45000",     // EC1: Middle value
            "23:59, 86340"      // EC1: Upper bound
    })
    @Tag("ECP")
    void parseFromStringToSeconds_validTimes_shouldReturnCorrectSeconds(String timeString, int expectedSeconds) {
        // Act
        int actualSeconds = tasksService.parseFromStringToSeconds(timeString);

        // Assert
        assertEquals(expectedSeconds, actualSeconds,
                "Time string " + timeString + " should convert to " + expectedSeconds + " seconds");
    }

    /**
     * Tests for invalid hour values using ECP
     * Hours must be between 0 and 23
     */
    @ParameterizedTest(name = "Invalid hour in time {0} should throw exception")
    @ValueSource(strings = {"-1:00", "24:00", "99:00"})
    @Tag("ECP")
    void parseFromStringToSeconds_invalidHours_shouldThrowException(String timeString) {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tasksService.parseFromStringToSeconds(timeString),
                "Should throw IllegalArgumentException for invalid hour: " + timeString);

        assertTrue(exception.getMessage().contains("Hour must be between 0-23"),
                "Exception message should mention hour range");
    }

    /**
     * Tests for invalid minute values using ECP
     * Minutes must be between 0 and 59
     */
    @ParameterizedTest(name = "Invalid minutes in time {0} should throw exception")
    @ValueSource(strings = {"12:-1", "12:60", "12:99"})
    @Tag("ECP")
    void parseFromStringToSeconds_invalidMinutes_shouldThrowException(String timeString) {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tasksService.parseFromStringToSeconds(timeString),
                "Should throw IllegalArgumentException for invalid minutes: " + timeString);

        assertTrue(exception.getMessage().contains("minutes between 0-59"),
                "Exception message should mention minutes range");
    }

    /**
     * Tests for invalid format strings using ECP
     */
    @ParameterizedTest(name = "Invalid format {0} should throw exception")
    @ValueSource(strings = {"", "12", "12:", ":30", "12:3", "12:300", "12-30", "12.30", "ab:cd"})
    @Tag("ECP")
    void parseFromStringToSeconds_invalidFormat_shouldThrowException(String timeString) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> tasksService.parseFromStringToSeconds(timeString),
                "Should throw IllegalArgumentException for invalid format: " + timeString);
    }

    /**
     * Tests for boundary values of hours
     */
    @ParameterizedTest(name = "Boundary hour value {0} should be handled correctly")
    @CsvSource({
            "0:30, 1800",     // BVA: minimum valid hour
            "1:30, 5400",     // BVA: minimum+1 valid hour
            "22:30, 81000",   // BVA: maximum-1 valid hour
            "23:30, 84600"    // BVA: maximum valid hour
    })
    @Tag("BVA")
    void parseFromStringToSeconds_boundaryHourValues_shouldReturnCorrectSeconds(String timeString, int expectedSeconds) {
        // Act
        int actualSeconds = tasksService.parseFromStringToSeconds(timeString);

        // Assert
        assertEquals(expectedSeconds, actualSeconds,
                "Time string " + timeString + " should convert to " + expectedSeconds + " seconds");
    }

    /**
     * Tests for boundary values of minutes
     */
    @ParameterizedTest(name = "Boundary minute value {0} should be handled correctly")
    @CsvSource({
            "12:00, 43200",    // BVA: minimum valid minutes
            "12:01, 43260",    // BVA: minimum+1 valid minutes
            "12:58, 46080",    // BVA: maximum-1 valid minutes
            "12:59, 46140"     // BVA: maximum valid minutes
    })
    @Tag("BVA")
    void parseFromStringToSeconds_boundaryMinuteValues_shouldReturnCorrectSeconds(String timeString, int expectedSeconds) {
        // Act
        int actualSeconds = tasksService.parseFromStringToSeconds(timeString);

        // Assert
        assertEquals(expectedSeconds, actualSeconds,
                "Time string " + timeString + " should convert to " + expectedSeconds + " seconds");
    }

    /**
     * Test to verify that the method handles properly formatted but numerically
     * invalid inputs correctly (hours and minutes contain numeric values but outside valid ranges)
     */
    @Test
    @DisplayName("Invalid numeric values in well-formatted time string")
    @Tag("Integration")
    void parseFromStringToSeconds_wellFormattedButInvalidNumbers_shouldThrowException() {
        // Arrange
        String invalidTimeString = "25:75";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tasksService.parseFromStringToSeconds(invalidTimeString));

        assertTrue(exception.getMessage().contains("Hour must be between 0-23"),
                "Exception should mention hour range constraint");
    }
}