package pomtimer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the pomodoro timer in which the user can use the pomodoro technique to complete their tasks.
 * A user can add tasks (these tasks can be named, can be indicated the number of pomodoros it should take, and
 * checked off to indicate completion) and perform different operations on the timer. The number of minutes on the
 * timer is dependent on the type of operation that is occurring (pomodoro, short break, or long break). Pomodoros
 * are followed by a short break, but is followed by a long break every four pomodoros.
 */
public class PomodoroTimer implements IPomodoroTimer {
    private List<Task> tasks;
    private ITask currentTask;
    private int pomodorosPassed;
    private final IWork pomodoro;
    private final IWork shortBreak;
    private final IWork longBreak;
    private boolean onPomodoro;
    private boolean onShortBreak;

    public PomodoroTimer() {
        this.tasks = new ArrayList<>();
        this.currentTask = null;
        this.pomodorosPassed = 0;
        this.pomodoro = new Pomodoro();
        this.shortBreak = new ShortBreak();
        this.longBreak = new LongBreak();
        this.onPomodoro = true;
        this.onShortBreak = false;
    }

    @Override
    public void addTask(String taskName, int numPomodoros) {
        if (taskName == null || numPomodoros <= 0) {
            throw new IllegalArgumentException("Cannot add a task with a null name or invalid number of pomodoro");
        }
        this.tasks.add(new Task(taskName, numPomodoros));
    }

    @Override
    public void checkTask(ITask task) {
        if (task == null || tasks.isEmpty() || !tasks.contains(task)) {
            throw new IllegalArgumentException("Cannot indicate completion this task");
        }
        task.check();
    }

    @Override
    public void setCurrentTask(ITask task) {
        if (task == null || tasks.isEmpty() || !tasks.contains(task)) {
            throw new IllegalArgumentException("Cannot set this task as the current task");
        }
        this.currentTask = task;
    }

    @Override
    public void removeTask(ITask task) {
        if (task == null || tasks.isEmpty() || !tasks.contains(task)) {
            throw new IllegalArgumentException("Cannot remove this task.");
        }
        this.tasks.remove(task);
    }

    @Override
    public String getRemainingTime() {
        if (onPomodoro) {
            return convertTime(pomodoro.getRemainingTime());
        } else if (onShortBreak) {
           return convertTime(shortBreak.getRemainingTime());
        } else {
            return convertTime(longBreak.getRemainingTime());
        }
    }

    private String convertTime(long remainingTime) {
        long minutes = (remainingTime / 1000)  / 60;
        int seconds = (int)((remainingTime / 1000) % 60);
        String sec = String.valueOf(seconds);
        if (sec.length() == 1) {
            sec = "0" + sec;
        }
        return minutes + ":" + sec;
    }

    @Override
    public void startTimer() {
        if (pomodorosPassed % 4 == 0 && !onPomodoro) {
            this.longBreak.startTimer();
        } else if (onPomodoro) {
            this.pomodoro.startTimer();
        } else {
            this.shortBreak.startTimer();
        }
    }

    @Override
    public void pauseTimer() {
        if (pomodorosPassed % 4 == 0 && !onPomodoro) {
            this.longBreak.pauseTimer();
        } else if (onPomodoro) {
            this.pomodoro.pauseTimer();
        } else {
            this.shortBreak.pauseTimer();
        }
    }

    @Override
    public void skipTimer() {
        if (pomodorosPassed % 4 == 0 && !onPomodoro) {
            this.longBreak.skipTimer();
            this.onPomodoro = true;
            this.longBreak.resetTimer();
        } else if (onPomodoro) {
            this.pomodoro.skipTimer();
            this.pomodorosPassed ++;
            this.onPomodoro = false;
            if (pomodorosPassed % 4 == 0) {
                this.onShortBreak = false;
            } else {
                this.onShortBreak = true;
            }
            this.pomodoro.resetTimer();
        } else {
            this.shortBreak.skipTimer();
            this.onPomodoro = true;
            this.shortBreak.resetTimer();
        }
    }

    @Override
    public void resetTimer() {
        if (pomodorosPassed % 4 == 0 && !onPomodoro) {
            this.longBreak.resetTimer();
        } else if (onPomodoro) {
            this.pomodoro.resetTimer();
        } else {
            this.shortBreak.resetTimer();
        }
    }

    @Override
    public void changeToNextOperation() {
        if ((pomodorosPassed - 1) % 4 == 0) {
            this.onPomodoro = true;
        } else if (onPomodoro) {
            this.onPomodoro = false;
            this.pomodorosPassed ++;
            if (pomodorosPassed % 4 == 0) {
                this.onShortBreak = false;
            } else {
                this.onShortBreak = true;
            }
        } else {
            this.onPomodoro = true;
        }
    }

    @Override
    public void goToPomodoro() {
        this.onPomodoro = true;
        this.onShortBreak = false;
        this.pomodoro.resetTimer();
    }

    @Override
    public void goToShortBreak() {
        this.onPomodoro = false;
        this.onShortBreak = true;
        this.shortBreak.resetTimer();
    }

    @Override
    public void goToLongBreak() {
        this.onPomodoro = false;
        this.onShortBreak = false;
        this.longBreak.resetTimer();
    }

//    @Override
//    public boolean isPomodoro() {
//        return false;
//    }
//
//    @Override
//    public boolean isShortBreak() {
//        return false;
//    }
//
//    @Override
//    public boolean isLongBreak() {
//        return false;
//    }

//    @Override
//    public void clearFinishedTasks() {
//        List<Task> updatedTasks = new ArrayList<>();
//        for (Task task : tasks) {
//            if (!task.isCompleted()) {
//                updatedTasks.add(task);
//            }
//        }
//        this.tasks = updatedTasks;
//    }

//    @Override
//    public void startPomodoro() {
//        this.pomodoro.startTimer();
//    }
//
//    @Override
//    public void pausePomodoro() {
//        this.pomodoro.pauseTimer();
//    }
//
//    @Override
//    public void skipPomodoro() {
//        this.pomodoro.skipTimer();
//    }
//
//    @Override
//    public void resetPomodoro() {
//        this.pomodoro.resetTimer();
//    }
//
//    @Override
//    public void startShort() {
//        this.shortBreak.startTimer();
//    }
//
//    @Override
//    public void pauseShort() {
//        this.shortBreak.pauseTimer();
//    }
//
//    @Override
//    public void skipShort() {
//        this.shortBreak.skipTimer();
//    }
//
//    @Override
//    public void resetShort() {
//        this.shortBreak.resetTimer();
//    }
//
//    @Override
//    public void startLong() {
//        this.longBreak.startTimer();
//    }
//
//    @Override
//    public void pauseLong() {
//        this.longBreak.pauseTimer();
//    }
//
//    @Override
//    public void skipLong() {
//        this.longBreak.skipTimer();
//    }
//
//    @Override
//    public void resetLong() {
//        this.longBreak.resetTimer();
//    }

    @Override
    public List<Task> getTasksList() {
        List<Task> taskList = new ArrayList<>();
        for (Task value : tasks) {
            Task task = new Task(value.getName(), value.getNumPomodoros());
            taskList.add(task);
        }
        return taskList;
    }

    @Override
    public int getNumPomodoros() {
        return pomodorosPassed;
    }

}
