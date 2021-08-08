package pomtimer.model;

/**
 * A class representing a task that has an inputted name/description and the number of pomodoros estimated to finish
 * the task. A task can be edited to change its description and number of pomodoros and can be checked off indicating
 * completion.
 */
public class Task implements ITask {
    private String name;
    private int numPomodoros;
    private boolean completed;

    public Task(String name, int numPomodoros) {
        this.name = name;
        this.numPomodoros = numPomodoros;
        this.completed = false;
    }

    public Task(String name, int numPomodoros, boolean completed) {
        this.name = name;
        this.numPomodoros = numPomodoros;
        this.completed = completed;
    }

    @Override
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name = name;
    }

    @Override
    public void setNumPomodoros(int numPomodoros) {
        if (numPomodoros <= 0) {
            throw new IllegalArgumentException("The number of pomodoros must be a positive integer.");
        }
        this.numPomodoros = numPomodoros;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getNumPomodoros() {
        return this.numPomodoros;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public void check() {
        this.completed = !this.completed;
    }
}
