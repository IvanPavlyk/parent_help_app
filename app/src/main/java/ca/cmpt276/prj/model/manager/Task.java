package ca.cmpt276.prj.model.manager;

import java.util.ArrayList;

import ca.cmpt276.prj.model.Child;

public class Task {

    String childName;
    String description;
    String TaskName;
    String portrait;
    ArrayList<Child> taskQueue;

    public Task(String taskName, String description) {
        this.TaskName = taskName;
        this.description = description;
        this.taskQueue = new ArrayList<>();
    }

    public void advanceTurn() {
        if (taskQueue.size() == 0 || taskQueue.size() == 1) return;
        Child next = taskQueue.get(1);
        Child head = taskQueue.remove(0);
        taskQueue.add(taskQueue.size(), head);
    }

    // Getters and Setters
    public Child getTaskHolder() {
        if (taskQueue.size() != 0) return taskQueue.get(0);
        else return null;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getTaskName() {
        return TaskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
