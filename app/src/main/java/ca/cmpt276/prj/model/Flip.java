package ca.cmpt276.prj.model;

public class Flip {

    private String time;
    private String pickerName;
    private CoinSide outcome;
    private boolean win;

    public Flip(String time, String pickerName, CoinSide outcome, boolean win) {
        this.time = time;
        this.pickerName = pickerName;
        this.outcome = outcome;
        this.win = win;
    }

    // Getters
    public String getTime() {
        return time;
    }

    public String getPickerName() {
        return pickerName;
    }

    public CoinSide getOutcome() {
        return outcome;
    }

    public boolean isWin() {
        return win;
    }
}
