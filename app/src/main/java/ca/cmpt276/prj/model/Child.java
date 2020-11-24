package ca.cmpt276.prj.model;

import ca.cmpt276.prj.model.manager.CoinSide;

/**
 * Child class used to create Child objects with name and pick attributes
 */
public class Child {

    private String name;
    private CoinSide pick;
    private String portrait;

    public Child(String name, CoinSide pick, String imageStr) {
        this.name = name;
        this.pick = pick;
        this.portrait = imageStr;
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

    public void setPortrait(String str){
        this.portrait = str;
    }

    public String getPortrait(){
        return this.portrait;
    }
}
