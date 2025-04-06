package tasks.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.model.Task;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DateService methods related to date and time handling
 */
@DisplayName("DateService Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DateServiceTest {

    private DateService dateService;
    private TasksService tasksService;

    @BeforeEach
    void setUp() {
        tasksService = new TasksService(null);
        dateService = new DateService(tasksService);
    }

    /**
     * Test for converting LocalDate to Date
     */
    @Test
    @Order(1)
    @DisplayName("Converting LocalDate to Date")
    void getDateValueFromLocalDate_validLocalDate_returnsCorrectDate() {
        // Arrange
        LocalDate localDate = LocalDate.of(2023, 5, 15);

        // Act
        Date result = dateService.getDateValueFromLocalDate(localDate);

        // Assert
        LocalDate convertedBack = result.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        assertEquals(localDate, convertedBack,
                "The converted Date should represent the same day as the original LocalDate");
    }

    /**
     * Test for converting Date to LocalDate
     */
    @Test
    @Order(2)
    @DisplayName("Converting Date to LocalDate")
    void getLocalDateValueFromDate_validDate_returnsCorrectLocalDate() {
        // Arrange
        LocalDate expected = LocalDate.of(2023, 5, 15);
        Date date = Date.from(expected.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Act
        LocalDate result = DateService.getLocalDateValueFromDate(date);

        // Assert
        assertEquals(expected, result,
                "The converted LocalDate should be the same as what was used to create the Date");
    }

    /**
     * Parameterized test for merging date with time string
     */
    @ParameterizedTest(name = "Merging date with time {0} should produce expected date-time")
    @Order(3)
    @MethodSource("provideDateAndTimeData")
    @Tag("Integration")
    void getDateMergedWithTime_validTimeStrings_mergesCorrectly(String timeString, int expectedHour, int expectedMinute) {
        // Arrange
        LocalDate localDate = LocalDate.of(2023, 5, 15);
        Date dateWithNoTime = dateService.getDateValueFromLocalDate(localDate);

        // Act
        Date mergedDateTime = dateService.getDateMergedWithTime(timeString, dateWithNoTime);

        // Assert
        // Convert back to check hours and minutes
        LocalDate resultDate = mergedDateTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        int resultHour = mergedDateTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .getHour();
        int resultMinute = mergedDateTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .getMinute();

        assertEquals(localDate, resultDate, "The date part should remain unchanged");
        assertEquals(expectedHour, resultHour, "Hour should be set to " + expectedHour);
        assertEquals(expectedMinute, resultMinute, "Minute should be set to " + expectedMinute);
    }

    /**
     * Test for exception when passing invalid time string
     */
    @Test
    @Order(4)
    @DisplayName("Invalid time string should throw exception")
    void getDateMergedWithTime_invalidTimeString_throwsException() {
        // Arrange
        LocalDate localDate = LocalDate.of(2023, 5, 15);
        Date dateWithNoTime = dateService.getDateValueFromLocalDate(localDate);
        String invalidTimeString = "25:70"; // Invalid hours and minutes

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> dateService.getDateMergedWithTime(invalidTimeString, dateWithNoTime),
                "Should throw IllegalArgumentException for invalid time: " + invalidTimeString);
    }

    /**
     * Test for extracting time string from Date
     */
    @Test
    @Order(5)
    @DisplayName("Extract time of day from Date")
    void getTimeOfTheDayFromDate_validDate_returnsCorrectTimeString() {
        // Arrange
        LocalDate localDate = LocalDate.of(2023, 5, 15);
        Date dateWithNoTime = dateService.getDateValueFromLocalDate(localDate);
        Date dateWithTime = dateService.getDateMergedWithTime("14:30", dateWithNoTime);

        // Act
        String timeString = dateService.getTimeOfTheDayFromDate(dateWithTime);

        // Assert
        assertEquals("14:30", timeString,
                "The extracted time string should match what was set");
    }

    /**
     * Provides test data for the getDateMergedWithTime test
     */
    private static Stream<Arguments> provideDateAndTimeData() {
        return Stream.of(
                Arguments.of("00:00", 0, 0),    // Minimum time
                Arguments.of("12:30", 12, 30),  // Standard time
                Arguments.of("23:59", 23, 59)   // Maximum time
        );
    }

    @Test
    @Disabled("Example of disabled test - for demonstration only")
    void disabledTestExample() {
        // This test is disabled and won't run
        fail("This test should not run as it's disabled");
    }
}