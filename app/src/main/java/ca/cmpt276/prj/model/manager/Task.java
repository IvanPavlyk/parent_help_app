package ca.cmpt276.prj.model.manager;

import java.util.ArrayList;

import ca.cmpt276.prj.model.Child;

public class Task {

    String ChildName;
    String description;
    String TaskName;

    public Task(String taskname, String description) {
        this.TaskName = taskname;
        this.description = description;
    }



    // Getters and Setters
    public String getChildName() {
        return ChildName;
    }

    public void setName(String childname) {
        this.ChildName = childname;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskname) {
        this.TaskName = taskname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
