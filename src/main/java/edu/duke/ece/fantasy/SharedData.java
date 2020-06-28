package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;

//sharedData is used for sharing player info between taskScheduler and messageHandler
public class SharedData {
    private Player player;

    public SharedData(){
    }

    public void setPlayer(Player player){this.player = player;}

    public Player getPlayer(){return this.player; }
}
