package edu.duke.ece.fantasy.World.worldgen;

import org.json.JSONObject;

public class Square {
  final String imageName;// e.g. "forestDense". Gets sent to client who renders "grass.png"
  final String id; // e.g. "F" the tag we use in the JSON file
  final String type; // e.g. "forest" this lets us distinguish between different images (corner,
                     // edge) with same behavior (all forest)
  // null for either of these next two indicates no rotation allowed
  final String rotId; // e.g. "F" id of a square we use if we rotate the tile clockwise (for eg rivers
                        // this changes NS to EW)

  public Square(String imageName, String id, String type, String rotId) {
    this.imageName = imageName;
    this.id = id;
    this.type = type;
    this.rotId = rotId;
  }

  public Square(JSONObject js) {
    this(js.getString("image"), js.getString("id"), js.getString("type"), js.optString("cw", null));
  }

  public String getImageName() {
    return imageName;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getRotId() {
    return rotId;
  }


}
