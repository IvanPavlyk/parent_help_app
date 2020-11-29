package ca.cmpt276.prj.model;

import java.util.ArrayList;

/**
 * A task_dialog object containing information on itself and the children waiting in turns to do it
 */
public class Task {

    String TaskName;
    String description;
    ArrayList<Child> taskQueue;

    public Task(String taskName, String description) {
        this.TaskName = taskName;
        this.description = description;
        this.taskQueue = new ArrayList<>();
        System.out.println("Adding children to task");
        for (Child child : Manager.getInstance().getChildrenList()) {
            taskQueue.add(taskQueue.size(), child);
        }
    }

    // Take current task_dialog holder off task_dialog, put it on the back of queue,
    // and make its next-in-line the new task_dialog holder
    public void advanceTurn() {
        if (taskQueue.size() == 0 || taskQueue.size() == 1) return;
        Child head = taskQueue.remove(0);
        taskQueue.add(taskQueue.size(), head);
    }

    // Get the current task_dialog holder
    public Child getTaskHolder() {
        if (taskQueue.size() != 0) return taskQueue.get(0);
        else return null;
    }

    // Task Queue Management
    public void appendChild(Child child) {
        taskQueue.add(taskQueue.size(), child);
    }

    public Child removeChild(Child child) {
        for (int i=0; i<taskQueue.size(); i++) {
            if (taskQueue.get(i).getName().equals(child.getName())) {
                return taskQueue.remove(i);
            }
        }
        return null;
    }

    public void wipeQueue() {
        taskQueue = new ArrayList<>();
    }

    // Getters and Setters
    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) { TaskName = taskName;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Child> getTaskQueue() {
        return taskQueue;
    }
}
