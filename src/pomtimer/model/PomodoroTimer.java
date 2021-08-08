package pomtimer.model;

import java.util.ArrayList;
import java.util.List;

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
    private boolean onLongBreak;

    public PomodoroTimer() {
        this.tasks = new ArrayList<>();
        this.currentTask = null;
        this.pomodorosPassed = 0;
        this.pomodoro = new Pomodoro();
        this.shortBreak = new ShortBreak();
        this.longBreak = new LongBreak();
        this.onPomodoro = true;
        this.onShortBreak = false;
        this.onLongBreak = false;
    }

    @Override
    public void addTask(String taskName, int numPomodoros) {
        if (taskName == null || numPomodoros <= 0) {
            throw new IllegalArgumentException("Cannot add a task with a null name or invalid number of pomodoro");
        }
        this.tasks.add(new Task(taskName, numPomodoros));
    }

    @Override
    public void checkTask(int index) {
        if (tasks.isEmpty() || index < 0 || index > tasks.size()) {
            throw new IllegalArgumentException("Cannot indicate completion this task");
        }
        this.tasks.get(index).check();
    }

    @Override
    public String getCurrentTask() {
        for (int i = 0; i < tasks.size(); i++) {
            if (!tasks.get(i).isCompleted()) {
                return tasks.get(i).getName();
            }
        }
        return "";
    }

    @Override
    public void editTaskName(String name, int index) throws IllegalArgumentException {
        if (tasks.isEmpty() || index < 0 || index >= tasks.size() || name == null) {
            throw new IllegalArgumentException("Cannot rename this task.");
        }
        tasks.get(index).setName(name);
    }

    @Override
    public void editNumPomodoros(int numPoms, int index) throws IllegalArgumentException {
        if (tasks.isEmpty() || index < 0 || index >= tasks.size() || numPoms <= 0 || numPoms > 100) {
            throw new IllegalArgumentException("Cannot edit the pomodoros for this task.");
        }
        tasks.get(index).setNumPomodoros(numPoms);
    }

    @Override
    public void removeTask(int index) {
        if (tasks.isEmpty() || index < 0 || index >= tasks.size()) {
            throw new IllegalArgumentException("Cannot remove this task.");
        }
        this.tasks.remove(index);
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
        long minutes = (remainingTime / 1000) / 60;
        int seconds = (int) ((remainingTime / 1000) % 60);
        String min = String.valueOf(minutes);
        if (min.length() == 1) {
            min = "0" + min;
        }
        String sec = String.valueOf(seconds);
        if (sec.length() == 1) {
            sec = "0" + sec;
        }
        return min + ":" + sec;
    }

    @Override
    public void startTimer() {
        if (this.onLongBreak || (pomodorosPassed % 4 == 0 && !onPomodoro && !onShortBreak)) {
            this.longBreak.startTimer();
        } else if (onPomodoro) {
            this.pomodoro.startTimer();
        } else {
            this.shortBreak.startTimer();
        }
    }

    @Override
    public void pauseTimer() {
        if (this.onLongBreak || (pomodorosPassed % 4 == 0 && !onPomodoro && !onShortBreak)) {
            this.longBreak.pauseTimer();
        } else if (onPomodoro) {
            this.pomodoro.pauseTimer();
        } else {
            this.shortBreak.pauseTimer();
        }
    }

    @Override
    public void skipTimer() {
        if (this.onLongBreak || (pomodorosPassed % 4 == 0 && !onPomodoro && !onShortBreak)) {
            this.longBreak.skipTimer();
            this.onPomodoro = true;
            this.onLongBreak = false;
            this.longBreak.resetTimer();
        } else if (onPomodoro) {
            this.pomodoro.skipTimer();
            this.pomodorosPassed++;
            this.onPomodoro = false;
            if (pomodorosPassed % 4 == 0) {
                this.onShortBreak = false;
                this.onLongBreak = true;
            } else {
                this.onShortBreak = true;
                this.onLongBreak = false;
            }
            this.pomodoro.resetTimer();
        } else {
            this.shortBreak.skipTimer();
            this.onPomodoro = true;
            this.onShortBreak = false;
            this.onLongBreak = false;
            this.shortBreak.resetTimer();
        }
    }

    @Override
    public void resetTimer() {
        if (this.onLongBreak || (pomodorosPassed % 4 == 0 && !onPomodoro && !onShortBreak)) {
            this.longBreak.resetTimer();
        } else if (onPomodoro) {
            this.pomodoro.resetTimer();
        } else {
            this.shortBreak.resetTimer();
        }
    }

    @Override
    public void changeToNextOperation() {
        if (this.onLongBreak || (pomodorosPassed % 4 == 0 && !onPomodoro && !onShortBreak)) {
            this.onPomodoro = true;
            this.onLongBreak = false;
            this.longBreak.resetTimer();
        } else if (onPomodoro) {
            this.onPomodoro = false;
            this.pomodorosPassed++;
            if (pomodorosPassed % 4 == 0) {
                this.onShortBreak = false;
                this.onLongBreak = true;
//                this.longBreak.resetTimer();
            } else {
                this.onShortBreak = true;
                this.onLongBreak = false;
//                this.shortBreak.resetTimer();
            }
            this.pomodoro.resetTimer();
        } else {
            this.onPomodoro = true;
            this.onShortBreak = false;
            this.onLongBreak = false;
            this.shortBreak.resetTimer();
        }
    }

    @Override
    public void goToPomodoro() {
        this.onPomodoro = true;
        this.onShortBreak = false;
        this.onLongBreak = false;
        this.pomodoro.resetTimer();
    }

    @Override
    public void goToShortBreak() {
        this.onPomodoro = false;
        this.onShortBreak = true;
        this.onLongBreak = false;
        this.shortBreak.resetTimer();
    }

    @Override
    public void goToLongBreak() {
        this.onPomodoro = false;
        this.onShortBreak = false;
        this.onLongBreak = true;
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

    @Override
    public List<ITask> getTasksList() {
        List<ITask> taskList = new ArrayList<>();
        for (Task value : tasks) {
            Task task = new Task(value.getName(), value.getNumPomodoros(), value.isCompleted());
            taskList.add(task);
        }
        return taskList;
    }

    @Override
    public int getNumPomodoros() {
        return pomodorosPassed;
    }

    @Override
    public boolean isOnPomodoro() {
        return onPomodoro;
    }

    @Override
    public boolean isOnShortBreak() {
        return onShortBreak;
    }

    @Override
    public boolean isOnLongBreak() {
        return onLongBreak;
    }

}
