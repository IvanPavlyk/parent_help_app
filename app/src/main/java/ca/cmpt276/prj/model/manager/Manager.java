package ca.cmpt276.prj.model.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import ca.cmpt276.prj.model.Child;
import ca.cmpt276.prj.model.manager.CoinSide;
import ca.cmpt276.prj.model.manager.Flip;
import ca.cmpt276.prj.model.manager.Task;

/**
 * Game singleton class used to handle all the logic connected to flip coin activity and manage children activity
 */
public class Manager {

    private static Manager manager;

    private ArrayList<Child> childrenList;
    private ArrayList<Flip> flipsRecord;
    private ArrayList<Task> taskList;
    private Child winner = null;

    public int size(){
        return taskList.size();
    }

    public static Manager getInstance() {
        if (manager == null) manager = new Manager();
        return manager;
    }

    public static void loadInstance(Manager instance) {
        if (instance != null) manager = instance;
    }

    private Manager() {
        this.childrenList = new ArrayList<>();
        this.flipsRecord = new ArrayList<>();
        taskList=new ArrayList<>();
    }

    public void addChild(Child child) {
        childrenList.add(childrenList.size(), child);
    }

    public void addChildToFront(Child child){
        childrenList.add(0, child);
    }

    public Child removeChild(Child child) {
        for (int i=0; i<childrenList.size(); i++) {
            if (childrenList.get(i).getName().equals(child.getName())) {
                return childrenList.remove(i);
            }  
        }
        return null;
    }

    public Child getChild(int index) {
        return childrenList.get(index);
    }

    public void wipeChildrens() {
        childrenList = new ArrayList<>();
    }

    public void wipeRecord() { flipsRecord = new ArrayList<>(); }

    private String time() {
        return Calendar.getInstance().getTime().toString();
    }

    public boolean flip() {

        // Initialization with front child as current picker
        if (childrenList == null || childrenList.size() == 0) throw new IllegalStateException();
        Random random = new Random();
        CoinSide outcome;
        Child currentPicker = childrenList.get(0);

        // Execute flip
        boolean result = random.nextBoolean();
        if (result) outcome = CoinSide.HEAD;
        else outcome = CoinSide.TAIL;

        // Determine win condition
        boolean win;
        if (currentPicker.getPick() == outcome) {
            winner = currentPicker;
            win = true;
        } else win = false;

        // Requeue the picker
        Child lastPicker = childrenList.remove(0);
        childrenList.add(lastPicker);

        // Record and return
        flipsRecord.add(new Flip(time(), currentPicker.getName(), outcome, win));
        return win;
    }

    public boolean plainCoinFlip(){
        Random random = new Random();
        return random.nextBoolean();
    }

    public ArrayList<Flip> getFilteredRecord(String name) {
        ArrayList<Flip> filteredRecord = new ArrayList<>();
        for (Flip flip : flipsRecord) {
            if (flip.getPickerName().equals(name)) filteredRecord.add(flip);
        }
        return filteredRecord;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

//    public Task removeTask(Task task) {
//        for (int i=0; i<taskList.size(); i++) {
//            if (taskList.get(i).getTaskName().equals(task.getTaskName())) {
//                return taskList.remove(i);
//            }
//        }
//        return null;
//    }

    public void wipeTasks() {
        taskList = new ArrayList<>();
    }

    // Returns image of child as a string, returns empty string if no correspondent child found
    public String getPortrait(String name) {
        for (Child child : childrenList) {
            if (child.getName().equals(name)) return child.getImageString();
        }
        return "";
    }

    public Task retrieving(int index){
        return taskList.get(index);
    }

    public int returnint(int i){
        return i;
    }

    public void add(Task newTask){
        taskList.add(newTask);
    }

    public void removeByIndex(int index){
        taskList.remove(retrieving(index));
    }

    public void EditTask(String task,String description,int index){
        removeByIndex(index);
        Task editTask=new Task(task,description);
        taskList.add(editTask);
    }

    // Getters and Setters
    public ArrayList<Child> getChildrenList() {
        return childrenList;
    }

    public ArrayList<Flip> getFlipsRecord() { return flipsRecord; }

    public Child getWinner() {
        return winner;
    }

    public void setChildrenList(ArrayList<Child> list){
        wipeChildrens();
        for(Child child : list){
            addChild(child);
        }
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
}
