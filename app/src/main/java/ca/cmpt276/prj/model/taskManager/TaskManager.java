package ca.cmpt276.prj.model.taskManager;

import java.util.ArrayList;

public class TaskManager {

    private static TaskManager taskManager;

    private ArrayList<Task> taskList;

    private TaskManager() { }

    public static TaskManager getInstance() {
        if (taskManager == null) taskManager = new TaskManager();
        return taskManager;
    }

    public static void loadInstance(TaskManager instance) {
        if (instance != null) taskManager = instance;
    }

    public void addTask(Task task) {
        taskList.add(taskList.size(), task);
    }

    public Task removeTask(Task task) {
        for (int i=0; i<taskList.size(); i++) {
            if (taskList.get(i).getName().equals(task.getName())) {
                return taskList.remove(i);
            }
        }
        return null;
    }

    public void wipeTasks() {
        taskList = new ArrayList<>();
    }

    // Getters and Setters
    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
}
