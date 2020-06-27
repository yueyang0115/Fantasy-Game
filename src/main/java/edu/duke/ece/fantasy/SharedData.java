package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;

public class SharedData {
    private Player player;

    public SharedData(){
        player = new Player();
    }

    public void setPlayer(Player player){this.player = player;}

    public Player getPlayer(){return this.player; }
}
