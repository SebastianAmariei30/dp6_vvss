package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.util.Date;

public class TasksService {

    private ArrayTaskList tasks;

    public TasksService(ArrayTaskList tasks){
        this.tasks = tasks;
    }

    public ObservableList<Task> getObservableList(){
        return FXCollections.observableArrayList(tasks.getAll());
    }
    public String getIntervalInHours(Task task){
        final int SECONDS_IN_MINUTE = 60;
        final int MINUTES_IN_HOUR = 60;

        int seconds = task.getRepeatInterval();
        int minutes = seconds / SECONDS_IN_MINUTE;
        int hours = minutes / MINUTES_IN_HOUR;
        minutes = minutes % MINUTES_IN_HOUR;

        return formTimeUnit(hours) + ":" + formTimeUnit(minutes); // hh:MM
    }
    public String formTimeUnit(int timeUnit){
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }

    public int parseFromStringToSeconds(String stringTime) { // hh:MM
        if (!stringTime.matches("^\\d{1,2}:\\d{2}$")) {
            throw new IllegalArgumentException("Invalid time format. Expected HH:MM.");
        }

        try {
            String[] units = stringTime.split(":");
            int hours = Integer.parseInt(units[0]);
            int minutes = Integer.parseInt(units[1]);

            if (hours < 0 || hours >= 24 || minutes < 0 || minutes >= 60) {
                throw new IllegalArgumentException("Hour must be between 0-23 and minutes between 0-59.");
            }

            return (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric values in time string.", e);
        }
    }


    public Iterable<Task> filterTasks(Date start, Date end){
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        Iterable<Task> filtered = tasksOps.incoming(start,end);
        //Iterable<Task> filtered = tasks.incoming(start, end);

        return filtered;
    }
}
