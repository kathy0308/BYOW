package byow.Core;

import byow.TileEngine.TETile;
import org.junit.*;

import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.HEIGHT;
import static org.junit.Assert.assertEquals;

public class EngineTest {
    @Test
    public void stringInputTest() {
        Engine engine = new Engine();
        TETile[][] world1 = engine.interactWithInputString("n22s");
        TETile[][] world2 = engine.interactWithInputString("n22s");
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                assertEquals(world1[i][j].description(), world2[i][j].description());
            }
        }
    }
    @Test
    public void stringInputTest2() {
        Engine engine = new Engine();
        TETile[][] world1 = engine.interactWithInputString("n22s");
        TETile[][] world2 = engine.interactWithInputString("n22s:q");
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                assertEquals(world1[i][j].description(), world2[i][j].description());
            }
        }
    }
    @Test
    public void stringInputTest3() {
        Engine engine = new Engine();
        TETile[][] world1 = engine.interactWithInputString("n22s");
        engine.interactWithInputString("n22s:q");
        TETile[][] world2 = engine.interactWithInputString("l");
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                assertEquals(world1[i][j].description(), world2[i][j].description());
            }
        }
    }
    @Test
    public void stringInputTest4() {
        Engine engine = new Engine();
        TETile[][] world1 = engine.interactWithInputString("n22sdd");
        engine.interactWithInputString("n22s:q");
        TETile[][] world2 = engine.interactWithInputString("ldd");
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                assertEquals(world1[i][j].description(), world2[i][j].description());
            }
        }
    }
}
