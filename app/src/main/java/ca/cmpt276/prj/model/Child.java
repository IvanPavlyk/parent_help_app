package ca.cmpt276.prj.model;

import ca.cmpt276.prj.model.coinManager.CoinSide;

/**
 * Child class used to create Child objects with name and pick attributes
 */
public class Child {

    private String name;
    private CoinSide pick;
    private int portrait;

    public Child(String name, CoinSide pick) {
        this.name = name;
        this.pick = pick;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name;}

    public CoinSide getPick() {
        return pick;
    }

    public void setPick(CoinSide pick) {
        this.pick = pick;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }
}
