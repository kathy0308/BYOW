package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;

public class Room {

    private int height;
    private int width;

    private int XX;
    private int YY;

    private HashSet<Room> neighbors;

    public Room(int h, int w, int x, int y) {
        height = h;
        width = w;
        XX = x;
        YY = y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getXX() {
        return XX;
    }

    public int getYY() {
        return YY;
    }

    public double roughDist(Room r) {
        return Math.pow((double) r.getXX() - (double) XX, 2)
                + Math.pow((double) r.getYY() - (double) YY, 2);
    }

    public void addNeighbor(Room r) {
        neighbors.add(r);
    }

    public boolean isNeighbor(Room r) {
        return neighbors.contains(r);
    }

    public void addDoor(int x, int y, TETile[][] world) {
        world[XX + x][YY + y] = Tileset.LOCKED_DOOR;
    }
}
