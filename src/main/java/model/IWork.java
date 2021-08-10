package main.java.model;

/**
 * An interface containing operations that can be applied to timers within the pomodoro, short break, and long break
 * periods.
 */
public interface IWork {
    /**
     * Starts the timer from the time that the timer has left off on (the remaining time).
     */
    void startTimer();

    /**
     * Pauses the timer.
     * @throws IllegalArgumentException if the timer is not currently running
     */
    void pauseTimer() throws IllegalArgumentException;

    /**
     * Skips the timer / the current period the user left off on.
     * @throws IllegalArgumentException if the timer is not currently running
     */
    void skipTimer() throws IllegalArgumentException;

    /**
     * Resets the timer to the respective operation time.
     * @throws IllegalArgumentException if the timer is not currently running
     */
    void resetTimer() throws IllegalArgumentException;

    /**
     * Returns the remaining time left on the timer.
     * @return the remaining time left on the timer.
     */
    long getRemainingTime();

    /**
     * Determines whether the timer is running (the user has started the timer).
     * @return whether the timer is running
     */
    boolean isRunning();

}
