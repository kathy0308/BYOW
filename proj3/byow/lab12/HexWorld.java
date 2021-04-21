package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        addHexagon(10, 15, 8, world, Tileset.WALL);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static void tesselate(int s) {

    }

    private static void addHexagon(int x, int y, int s, TETile[][] world, TETile tile) {
        int height = 2 * s;
        int width = 3 * s - 2;

        //draw top
        int l = s;
        for (int i = 0; i < (height / 2); i++) {
            drawLine(x + (width - l)/2,y - i,l, world, tile);
            l += 2;
        }
        //draw bottom
        l = width;
        for (int i = (height / 2); i < height; i++) {
            drawLine(x + (width - l)/2,y - i,l, world, tile);
            l -= 2;
        }
    }

    private static void drawLine(int x, int y, int l, TETile[][] world, TETile tile) {
        for (int i = 0; i < l; i++) {
            world[x + i][y] = tile;
        }
    }
}
