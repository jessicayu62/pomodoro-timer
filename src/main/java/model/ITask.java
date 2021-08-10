package main.java.model;

/**
 * This interface represents a type of task that a user aims to work on using the model.Pomodoro Timer.
 */
public interface ITask {
    /**
     * Sets this task with the given name/description.
     * @param name the name/description of the task the user aims to work on.
     * @throws IllegalArgumentException if the given name is null.
     */
    void setName(String name) throws IllegalArgumentException;

    /**
     * Sets this task to have the goal of completing in the given number of pomodoros.
     * @param numPomodoros the number of pomodoros the user aims to complete this task in.
     * @throws IllegalArgumentException if the number of pomodoros is a non-positive integer.
     */
    void setNumPomodoros(int numPomodoros) throws IllegalArgumentException;

    /**
     * Returns this task's name/description.
     * @return the name/description of this task.
     */
    String getName();

    /**
     * Returns the number of pomodoros the task should be completed in.
     * @return the number of pomodoros the task should be completed in.
     */
    int getNumPomodoros();

    /**
     * Returns whether this task has been checked off as completed by the user.
     * @return whether this task has been checked off as completed by the user.
     */
    boolean isCompleted();

    /**
     * Checks off this task to either be completed or not completed.
     */
    void check();

}
