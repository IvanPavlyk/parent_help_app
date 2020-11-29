package ca.cmpt276.prj.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Game singleton class used to handle all the logic connected to flip coin activity and manage children activity
 */
public class Manager {

    private static Manager manager;

    private ArrayList<Child> childrenList;
    private ArrayList<Flip> flipsRecord;
    private ArrayList<Task> taskList;

    private Child winner;
    private int breathsRemaining;
    private int breathsTaken;
    private BreathState breathState;

    // Base Methods
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
        this.taskList = new ArrayList<>();
        this.winner = null;
        this.breathState = BreathState.DONE;
    }

    // Child Management Methods (Also directly connects with task queue)
    public void appendChild(Child child) {
        childrenList.add(childrenList.size(), child);
        for (Task task : taskList) {
            task.appendChild(child);
        }
    }

    public void prependChild(Child child){
        childrenList.add(0, child);
    }

    public Child removeChild(Child child) {
        for (int i=0; i<childrenList.size(); i++) {
            if (childrenList.get(i).getName().equals(child.getName())) {
                for (Task task : taskList) {
                    task.removeChild(child);
                }
                return childrenList.remove(i);
            }  
        }
        return null;
    }

    public Child getChild(int index) {
        return childrenList.get(index);
    }

    public void wipeChildren() {
        childrenList = new ArrayList<>();
        for (Task task : taskList) {
            task.wipeQueue();
        }
    }

    // Flip Record Management Methods
    public ArrayList<Flip> getFilteredRecord(String name) {
        ArrayList<Flip> filteredRecord = new ArrayList<>();
        for (Flip flip : flipsRecord) {
            if (flip.getPickerName().equals(name)) filteredRecord.add(flip);
        }
        return filteredRecord;
    }

    public void wipeRecord() { flipsRecord = new ArrayList<>(); }

    private String time() {
        return Calendar.getInstance().getTime().toString();
    }

    // Coin Flip Methods
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

    // Task Management Methods
    public int taskListSize(){
        return taskList.size();
    }

    public void addTask(Task newTask){
        taskList.add(newTask);
    }

    public Task removeTask(Task task) {
        for (int i=0; i<taskList.size(); i++) {
            if (taskList.get(i).getTaskName().equals(task.getTaskName())) {
                return taskList.remove(i);
            }
        }
        return null;
    }

    public void wipeTasks() {
        taskList = new ArrayList<>();
    }

    public Task getTask(int index){
        return taskList.get(index);
    }

    public void removeTask(int index){
        taskList.remove(getTask(index));
    }

    public void editTask(String task, String description, int index){
        removeTask(index);
        Task editTask = new Task(task,description);
        taskList.add(editTask);
    }

    // Breath Management Methods
    public void inhale() {
        // starts to inhale
        if (breathState == BreathState.INHALE) {
            throw new IllegalStateException("Already inhaling!");
        }
        breathState = BreathState.INHALE;
    }

    public void exhale() {
        // starts to exhale
        if (breathState == BreathState.DONE) {
            throw new IllegalStateException("You have to inhale before exhaling!");
        }
        if (breathState == BreathState.EXHALE) {
            throw new IllegalStateException("Already exhaling!");
        }
        breathState = BreathState.EXHALE;
        breathsTaken++;
        breathsRemaining--;
    }

    public void done() {
        // Reset breathState;
        breathState = BreathState.DONE;
    }

    // Getters and Setters
    public ArrayList<Child> getChildrenList() {
        return childrenList;
    }

    public ArrayList<Flip> getFlipsRecord() { return flipsRecord; }

    public Child getWinner() {
        return winner;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public int getBreathsRemaining() {
        return breathsRemaining;
    }

    public void setBreathsRemaining(int breathsRemaining) {
        this.breathsRemaining = breathsRemaining;
    }

    public int getBreathsTaken() {
        return breathsTaken;
    }

    public void setBreathsTaken(int breathsTaken) {
        this.breathsTaken = breathsTaken;
    }

    public BreathState getBreathState() {
        return breathState;
    }

    public void setBreathState(BreathState breathState) {
        this.breathState = breathState;
    }
}
