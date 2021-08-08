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
     * @param index the index of the task to mark as complete from task list
     * @throws IllegalArgumentException if the given index is out of bounds or if the list of tasks is empty.
     */
    void checkTask(int index) throws IllegalArgumentException;

    /**
     * Returns the task name/description that the user is currently working on (the first task in the list of tasks
     * that is not yet complete). Returns an empty string if the list of tasks is empty or if all tasks have been completed.
     */
    String getCurrentTask();

    /**
     * Edits the task at the given index to be the given name.
     *
     * @param name  the task name to be changed to.
     * @param index the index of the task to rename from task list
     * @throws IllegalArgumentException if the index is negative or out of bounds or if the task list is empty.
     */
    void editTaskName(String name, int index) throws IllegalArgumentException;

    /**
     * Edits the number of pomodoros for the task at the given index.
     *
     * @param numPoms the numbers of pomodoros to be changed to.
     * @param index   the index of the task to rename from task list
     * @throws IllegalArgumentException if the index is negative or out of bounds or if the poms is non-positive or greater than 100.
     */
    void editNumPomodoros(int numPoms, int index) throws IllegalArgumentException;

    /**
     * Removes the task at the given index from the list of tasks for this pomodoro timer.
     *
     * @param index the index of the task to remove from task list
     * @throws IllegalArgumentException if the index is negative or out of bounds or if the task list is empty.
     */
    void removeTask(int index) throws IllegalArgumentException;


    String getRemainingTime();
//    /**
//     * Removes the completed tasks from the list of tasks.
//     */
//    void clearFinishedTasks();

    void startTimer();

    void pauseTimer();

    void skipTimer();

    void resetTimer();

    void changeToNextOperation();

    void goToPomodoro();

    void goToShortBreak();

    void goToLongBreak();

    /**
     * Returns a deep copy of the list of tasks that the user has entered.
     *
     * @return list of tasks that the user has entered.
     */
    List<ITask> getTasksList();

    /**
     * Returns the number of pomodoros that the user has gone through so far.
     *
     * @return the number of pomodoros that the user has gone through so far.
     */
    int getNumPomodoros();

    boolean isOnPomodoro();

    boolean isOnShortBreak();

    boolean isOnLongBreak();

}


//    /**
//     * Starts/Resumes the pomodoro timer.
//     */
//    void startPomodoro();
//
//    /**
//     * Pauses the pomodoro timer.
//     */
//    void pausePomodoro();
//
//    /**
//     * Skips the pomodoro timer.
//     */
//    void skipPomodoro();
//
//    /**
//     * Resetes the pomodoro timer.
//     */
//    void resetPomodoro();
//
//    /**
//     * Starts the short break timer.
//     */
//    void startShort();
//
//    /**
//     * Pauses the short break timer.
//     */
//    void pauseShort();
//
//    /**
//     * Skips the short break timer.
//     */
//    void skipShort();
//
//    /**
//     * Resets the short break timer.
//     */
//    void resetShort();
//
//    /**
//     * Starts the long break.
//     */
//    void startLong();
//
//    /**
//     * Pauses the long break.
//     */
//    void pauseLong();
//
//    /**
//     * Skips the long break.
//     */
//    void skipLong();
//
//    /**
//     * Resets the long break.
//     */
//    void resetLong();