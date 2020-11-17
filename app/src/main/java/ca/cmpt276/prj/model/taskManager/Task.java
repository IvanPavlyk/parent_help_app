package ca.cmpt276.prj.model.taskManager;

import java.util.ArrayList;

import ca.cmpt276.prj.model.Child;

public class Task {

    String name;
    String description;
    ArrayList<Child> queue;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        queue = new ArrayList<>();
    }

    public Child whoseTurn () {
        return queue.get(0);
    }

    public Child whoseNext() {
        return queue.get(1);
    }

    public void advanceTurn() {
        Child child = queue.remove(0);
        queue.add(queue.size(), child);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Child> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Child> queue) {
        this.queue = queue;
    }
}
