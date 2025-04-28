package tasks.model;


import org.apache.log4j.Logger;

import java.util.*;

public class ArrayTaskList extends TaskList{

    private Task[] tasks;
    private int numberOfTasks;
    private int currentCapacity;
    private static final Logger log = Logger.getLogger(ArrayTaskList.class.getName());
    private class ArrayTaskListIterator implements Iterator<Task> {
        private int cursor;
        private int lastCalled = -1;
        @Override
        public boolean hasNext() {
            return cursor < numberOfTasks;
        }

        @Override
        public Task next() {
            if (!hasNext()){
                log.error("next iterator element doesn't exist");
                throw new NoSuchElementException("No next element");
            }
            lastCalled = cursor;
            return getTask(cursor++);
        }

        @Override
        public void remove() {
            if (lastCalled == -1){
                throw new IllegalStateException();
            }
            ArrayTaskList.this.remove(getTask(lastCalled));
            cursor = lastCalled;
            lastCalled = -1;
        }
    }
    public ArrayTaskList(){
        currentCapacity = 10;
        this.tasks = new Task[currentCapacity];
    }

    @Override
    public Iterator<Task> iterator() {
        return new ArrayTaskListIterator();
    }

    @Override
    public void add(Task task){
        if (task.equals(null)) throw new NullPointerException("Task shouldn't be null");
        if (Objects.equals(task.getTitle(), "") || Objects.equals(task.getTitle(),null))
            throw new IllegalArgumentException("Description can not be empty");
        if (task.getStartTime().before(new Date(0))) {
            throw new IllegalArgumentException("Start date cannot be before 1 Jan 1970.");
        }

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(task.getStartTime());
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if (hour < 8 || hour >= 16) {
//            throw new IllegalArgumentException("Start time must be between 08:00 and 16:00.");
//        }

        if (numberOfTasks == currentCapacity-1){
            currentCapacity = currentCapacity * 2;
            Task[] withAddedTask = new Task[currentCapacity];
            System.arraycopy(tasks,0,withAddedTask,0,tasks.length);
            this.tasks = withAddedTask;
        }
        this.tasks[numberOfTasks] = task;
        this.numberOfTasks++;
    }
    @Override
    public boolean remove(Task task){
        int indexOfTaskToDelete = -1;
        for(int i = 0; i < tasks.length; i++){
            if (task.equals(tasks[i])){
                indexOfTaskToDelete = i;
                break;
            }
        }
        if (indexOfTaskToDelete >= 0){
            this.numberOfTasks--;
            System.arraycopy(tasks, indexOfTaskToDelete+1,tasks,indexOfTaskToDelete,
                    numberOfTasks-indexOfTaskToDelete+1);
            return true;
        }
        return false;
    }
    @Override
    public int size(){
        return this.numberOfTasks;
    }
    @Override
    public Task getTask(int index){
        if (index < 0 || index > size()-1) {
            log.error("not existing index");
            throw new IndexOutOfBoundsException("Index not found");
        }


        return tasks[index];
    }

    @Override
    public List<Task> getAll() {
        ArrayList<Task> tks=new ArrayList<>();
        for (int i=0; i<this.numberOfTasks;i++)
            tks.add(this.tasks[i]);
        return tks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayTaskList that = (ArrayTaskList) o;

        if (numberOfTasks != that.numberOfTasks) return false;
        int i = 0;
        for (Task a : this){
            if (!a.equals(((ArrayTaskList) o).getTask(i))){
                return false;
            }
            i++;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(tasks);
        result = 31 * result + numberOfTasks;
        result = 31 * result + currentCapacity;
        return result;
    }

    @Override
    public String toString() {
        return "ArrayTaskList{" +
                "tasks=" + Arrays.toString(tasks) +
                ", numberOfTasks=" + numberOfTasks +
                ", currentCapacity=" + currentCapacity +
                '}';
    }
    @Override
    protected ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList tasks = new ArrayTaskList();
        for (int i = 0; i < this.tasks.length; i++){
            tasks.add(this.getTask(i));
        }
        return tasks;

    }
}