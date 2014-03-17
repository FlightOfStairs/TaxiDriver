import java.util.ArrayList;


public abstract class State {
    private int currentState;

    protected ArrayList<String> states;

    public State() {
        states = new ArrayList<String>();
        states.add("idle");
        addStates();

        currentState = 0;
    }

    public abstract void addStates();
    public String getState() { return states.get(currentState); }

    public void nextState() {
        currentState++;
        if (currentState == states.size()) currentState = 0;
    }

}
