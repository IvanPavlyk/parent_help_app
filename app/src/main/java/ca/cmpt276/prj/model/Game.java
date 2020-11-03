package ca.cmpt276.prj.model;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    private static Game game;

    ArrayList<Child> childrenList;
    ArrayList<Flip> flipsRecord;
    Child winner = null;

    public static Game getInstance() {
        if (game == null) game = new Game();
        return game;
    }

    private Game() {
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
        return "NULL";
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
        } else {
            win = false;
        }

        // Requeue the picker
        Child lastPicker = childrenList.remove(0);
        childrenList.add(lastPicker);

        // Record and return
        flipsRecord.add(new Flip(time(), currentPicker.getName(), outcome, win));
        return win;
    }

    public boolean plainCoinFlip(boolean pickedHeads){
        Random random = new Random();
        boolean result = random.nextBoolean();
        //result = true for heads, false for tails
        return result == pickedHeads;
    }

    // Getters
    public ArrayList<Child> getChildrenList() {
        return childrenList;
    }

    public Child getWinner() {
        return winner;
    }
}
