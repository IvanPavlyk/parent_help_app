package ca.cmpt276.prj.model.coinManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import ca.cmpt276.prj.model.Child;

/**
 * Game singleton class used to handle all the logic connected to flip coin activity and manage children activity
 */
public class CoinManager {

    private static CoinManager coinManager;

    private ArrayList<Child> childrenList;
    private ArrayList<Flip> flipsRecord;
    private Child winner = null;

    public static CoinManager getInstance() {
        if (coinManager == null) coinManager = new CoinManager();
        return coinManager;
    }

    public static void loadInstance(CoinManager instance) {
        if (instance != null) coinManager = instance;
    }

    private CoinManager() {
        this.childrenList = new ArrayList<>();
        this.flipsRecord = new ArrayList<>();
    }

    public void addChild(Child child) {
        childrenList.add(childrenList.size(), child);
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
}
