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

    public PomodoroTimer() {
        this.tasks = new ArrayList<>();
        this.currentTask = null;
        this.pomodorosPassed = 0;
        this.pomodoro = new Pomodoro();
        this.shortBreak = new ShortBreak();
        this.longBreak = new LongBreak();
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
    public void clearFinishedTasks() {
        List<Task> updatedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                updatedTasks.add(task);
            }
        }
        this.tasks = updatedTasks;
    }

    @Override
    public void startPomodoro() {
        this.pomodoro.startTimer();
    }

    @Override
    public void pausePomodoro() {
        this.pomodoro.pauseTimer();
    }

    @Override
    public void skipPomodoro() {
        this.pomodoro.skipTimer();
    }

    @Override
    public void resetPomodoro() {
        this.pomodoro.resetTimer();
    }

    @Override
    public void startShort() {
        this.shortBreak.startTimer();
    }

    @Override
    public void pauseShort() {
        this.shortBreak.pauseTimer();
    }

    @Override
    public void skipShort() {
        this.shortBreak.skipTimer();
    }

    @Override
    public void resetShort() {
        this.shortBreak.resetTimer();
    }

    @Override
    public void startLong() {
        this.longBreak.startTimer();
    }

    @Override
    public void pauseLong() {
        this.longBreak.pauseTimer();
    }

    @Override
    public void skipLong() {
        this.longBreak.skipTimer();
    }

    @Override
    public void resetLong() {
        this.longBreak.resetTimer();
    }

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
        return AbstractWork.getPomodoroCounter();
    }

}
