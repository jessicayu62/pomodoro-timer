package main.java.model;

/**
 * This class represents a long break of 15 minutes which occurs every 4 pomodoros.
 */
public class LongBreak extends AbstractWork {
    public LongBreak() {
//        super(900);
        super(4);
    }
}
