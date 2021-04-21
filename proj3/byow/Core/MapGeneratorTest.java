package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.Random;

import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.HEIGHT;

public class MapGeneratorTest {
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Random r = new Random();

        long seed = r.nextLong();
        //long seed = -2043309164258407346L;
        System.out.println(seed);

        Random r2 = new Random(seed);
        MapGenerator mg = new MapGenerator(r2);

        TETile[][] finalWorldFrame = mg.generateMap();
        ter.renderFrame(finalWorldFrame);
        
    }
}
