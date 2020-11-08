package ca.cmpt276.prj.model;

/**
 * Child class used to create Child objects with name and pick attributes
 */
public class Child {

    private String name;
    private CoinSide pick;

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
}
