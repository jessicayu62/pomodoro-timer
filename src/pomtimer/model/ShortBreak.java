package pomtimer.model;

/**
 * This class represents a short break of 5 minutes which occurs every pomodoro (except every fourth pomodoro).
 */
public class ShortBreak extends AbstractWork {
    public ShortBreak() {
        super(300);
    }
}
