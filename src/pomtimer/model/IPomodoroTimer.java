package pomtimer.model;

import java.util.List;

/**
 * This interface represents a pomodoro timer used to practice the pomodoro technique to increase a user's productivity.
 */
public interface IPomodoroTimer {
    /**
     * Adds a task with the given name and the number of pomodoros to this timer's list of tasks.
     *
     * @param taskName     the name/description of the task.
     * @param numPomodoros the number of pomodoros estimated to complete the task.
     * @throws IllegalArgumentException if the taskName or numPomodoros is null.
     */
    void addTask(String taskName, int numPomodoros) throws IllegalArgumentException;

    /**
     * Checks off the given task to indicate completion of the task.
     *
     * @param task the task to be checked off as completed.
     * @throws IllegalArgumentException if the given task is null or if the task is nonexistent or if the list of tasks
     *                                  * is empty.
     */
    void checkTask(ITask task) throws IllegalArgumentException;

    /**
     * Sets the given task as the current task the user is working on completing.
     *
     * @param task the task the user is currently working on.
     * @throws IllegalArgumentException if the given task is null or if the task is nonexistent or if the list of tasks
     *                                  is empty.
     */
    void setCurrentTask(ITask task) throws IllegalArgumentException;

    /**
     * Removes the given task from the list of tasks for this pomodoro timer.
     *
     * @param task the task to be deleted/removed.
     * @throws IllegalArgumentException if the given task is null or if the task is nonexistent or if the list of tasks
     *                                  is empty.
     */
    void removeTask(ITask task) throws IllegalArgumentException;

    /**
     * Removes the completed tasks from the list of tasks.
     */
    void clearFinishedTasks();

    /**
     * Starts/Resumes the pomodoro timer.
     */
    void startPomodoro();

    /**
     * Pauses the pomodoro timer.
     */
    void pausePomodoro();

    /**
     * Skips the pomodoro timer.
     */
    void skipPomodoro();

    /**
     * Resetes the pomodoro timer.
     */
    void resetPomodoro();

    /**
     * Starts the short break timer.
     */
    void startShort();

    /**
     * Pauses the short break timer.
     */
    void pauseShort();

    /**
     * Skips the short break timer.
     */
    void skipShort();

    /**
     * Resets the short break timer.
     */
    void resetShort();

    /**
     * Starts the long break.
     */
    void startLong();

    /**
     * Pauses the long break.
     */
    void pauseLong();

    /**
     * Skips the long break.
     */
    void skipLong();

    /**
     * Resets the long break.
     */
    void resetLong();

    /**
     * Returns a deep copy of the list of tasks that the user has entered.
     * @return  list of tasks that the user has entered.
     */
    List<Task> getTasksList();

    /**
     * Returns the number of pomodoros that the user has gone through so far.
     * @return the number of pomodoros that the user has gone through so far.
     */
    int getNumPomodoros();


}
