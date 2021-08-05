package pomtimer.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An abstract class for the timers for a pomodoro, a short break, and a long break. A user can start/resume and pause
 * the timer, skip the timer, and restart the timer. This class also keeps track of the number of pomodoros that the
 * user has passed through.
 */
public abstract class AbstractWork implements IWork {
    protected Timer timer;
    protected boolean isRunning;  // If timer is running, then the pause button is showing. Else button is start.
    protected long interval;  // One second interval between tasks
    protected long elapsedTime;  // The number of milliseconds that have passed as the time runs
    protected long duration;  // How long the timer is in seconds
//    protected static int pomodoroCounter = 0;

    public AbstractWork(int duration) {
        this.timer = new Timer();
        this.isRunning = false;
        this.interval = 1000;
        this.elapsedTime = 0;
        this.duration = duration * 1000L;
    }

    @Override
    public void startTimer() {
        if (!this.isRunning) {
            this.isRunning = true;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    elapsedTime += AbstractWork.this.interval;
                    System.out.println(getRemainingTime());
                    if (elapsedTime >= duration) {
                        timer.cancel();
//                    pomodoroCounter++;
                    }
                }
            }, 0, 1000);
        }
    }

    @Override
    public void pauseTimer() {
//        if (!this.isRunning) {
//            throw new IllegalArgumentException("Impossible, cannot pause the timer when it has not been started.");
//        }
        this.isRunning = false;
        timer.cancel();
    }

    @Override
    public void skipTimer() {
//        if (!this.isRunning) {
//            throw new IllegalArgumentException("Impossible, cannot skip the timer when it has not been started.");
//        }
        this.isRunning = false;
        timer.cancel();
    }

    @Override
    public void resetTimer() {
//        if (!this.isRunning) {
//            throw new IllegalArgumentException("Impossible, cannot restart the timer when it has not been started.");
//        }
        this.isRunning = false;
        timer.cancel();
        this.elapsedTime = 0;
    }

    @Override
    public long getRemainingTime() {
        return this.duration - this.elapsedTime;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

//    public static int getPomodoroCounter() {
//        return pomodoroCounter;
//    }
}
