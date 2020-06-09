package edu.duke.ece.fantasy.json;

public class BattleAction {
    private int attackerID;
    private int attackeeID;
    private String actionType = "normal"; //"normal", "magical"


    public BattleAction() {
    }

    public BattleAction(int attacker, int attackee, String actionType) {
        this.attackerID = attacker;
        this.attackeeID = attackee;
        this.actionType = actionType;
    }

    public int getAttackerID() { return attackerID; }

    public void setAttackerID(int attackerID) { this.attackerID = attackerID; }

    public int getAttackeeID() { return attackeeID; }

    public void setAttackeeID(int attackeeID) { this.attackeeID = attackeeID; }

    public String getActionType() { return actionType; }

    public void setActionType(String actionType) { this.actionType = actionType; }
}
