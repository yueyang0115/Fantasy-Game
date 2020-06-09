package edu.duke.ece.fantasy.worldgen;

public class InvalidMapDataException extends Exception {
  final long serialVersionUID = 0;
  public InvalidMapDataException(String mesg){
    super(mesg);
  }
}
