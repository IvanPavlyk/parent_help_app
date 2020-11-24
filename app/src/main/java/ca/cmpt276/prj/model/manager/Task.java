package ca.cmpt276.prj.model.manager;

import java.util.ArrayList;

import ca.cmpt276.prj.model.Child;

public class Task {

    String childName;
    String description;
    String TaskName;
    String portrait;

    public Task(String taskname, String description) {
        this.TaskName = taskname;
        this.description = description;
    }



    // Getters and Setters
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
