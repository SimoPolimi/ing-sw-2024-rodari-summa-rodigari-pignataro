package it.polimi.ingsw.gc42.classes;

public class Player {
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isFirst;
    private Token token;
    private int points;
    private Objective objective;
    private Hand hand;
    private PlayField playField;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Player(boolean isFirst, int points, Token token, Objective objective) {

        this.isFirst = isFirst;
        this.points = points;
        this.token = token;
        this.objective = objective;

    }

    public void notifyMaxPointsReached(){

    }

    public void setStartingHand(){
        // TODO: draw
    }






}
