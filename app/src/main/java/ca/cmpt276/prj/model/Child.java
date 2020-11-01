package ca.cmpt276.prj.model;

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

    public CoinSide getPick() {
        return pick;
    }

    public void setPick(CoinSide pick) {
        this.pick = pick;
    }
}
