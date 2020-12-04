package ca.cmpt276.prj.model;

/**
 * Flip class used to create objects that have time, pickerName, outcome and win attributes which
 * are used to save the history of flips further
 */
public class Flip {

    private String time;
    private String pickerName;
    private String pickerPortrait;
    private CoinSide outcome;
    private boolean win;

    public Flip(String time, String pickerName, String pickerPortrait, CoinSide outcome, boolean win) {
        this.time = time;
        this.pickerName = pickerName;
        this.pickerPortrait = pickerPortrait;
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

    public String getPickerPortrait() { return pickerPortrait; }

    public CoinSide getOutcome() {
        return outcome;
    }

    public boolean isWin() {
        return win;
    }
}
