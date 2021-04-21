package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.HEIGHT;

public class MapGenerator {

    private static final int MIN_NUM_ROOMS = 15;
    private static final int MAX_NUM_ROOMS = 20;
    private static final int MIN_ROOM_WIDTH = 4;
    private static final int MAX_ROOM_WIDTH = 8;
    private static final int MIN_ROOM_HEIGHT = 4;
    private static final int MAX_ROOM_HEIGHT = 8;
    private static final int HUD_SPACE = 5;

    private List<Room> rooms;

    private final Random r;

    public MapGenerator(Random r) {
        this.r = r;
    }

    public TETile[][] generateMap() {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        rooms = new ArrayList<>();

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.GRASS;
            }
        }

        //Rooms
        int numRooms = MIN_NUM_ROOMS + r.nextInt(MAX_NUM_ROOMS - MIN_NUM_ROOMS);
        int i = 0;
        while (i < numRooms) {
            //Generate random point in map (with minimum space for a room)
            int x = r.nextInt(WIDTH - MIN_ROOM_WIDTH);
            int y = r.nextInt(HEIGHT - MIN_ROOM_HEIGHT - HUD_SPACE);

            //random size room
            int h = Math.min(HEIGHT - y, MIN_ROOM_HEIGHT
                    + r.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT));
            int w = Math.min(WIDTH - x, MIN_ROOM_WIDTH
                    + r.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH));
            Room room = new Room(h, w, x, y);

            if (!isOverlap(room, world)) {
                drawRoom(room, world);
                rooms.add(room);
                i++;
            }
        }

        UnionFind roomsUF = new UnionFind(rooms.size());

        List<Room> roomsMaster = new ArrayList<>(rooms.size());
        roomsMaster.addAll(rooms);

        Room virtualMiddle = new Room(0, 0, WIDTH / 2, HEIGHT / 2);
        sortByDistTo(roomsMaster, virtualMiddle);

        for (int roomIndex1 = 0; roomIndex1 < roomsMaster.size(); roomIndex1++) {
            sortByDistTo(rooms, roomsMaster.get(roomIndex1));
            int numConnections = 0;
            for (int roomIndex2 = 1; roomIndex2 < rooms.size(); roomIndex2++) {
                int masterIndex1 = roomIndex1;
                int masterIndex2 = roomsMaster.indexOf(rooms.get(roomIndex2));
                if (roomsUF.isConnected(masterIndex1, masterIndex2)) {
                    continue;
                }
                connect(roomsMaster.get(masterIndex1), roomsMaster.get(masterIndex2), world);
                roomsUF.connect(masterIndex1, masterIndex2);
                numConnections++;
                if (numConnections > 0) {
                    break;
                }
            }
        }
        return world;
    }

    private List<Room> connect(Room r1, Room r2, TETile[][] world) {
        if (verticallyAligned(r1, r2)) {
            List<Room> newRoom = new ArrayList<>();
            newRoom.add(drawVerticalHallway(r1, r2, world));
            return newRoom;
        } else if (horizontallyAligned(r1, r2)) {
            List<Room> newRoom = new ArrayList<>();
            newRoom.add(drawHorizontalHallway(r1, r2, world));
            return newRoom;
        } else {
            return drawBentHallway(r1, r2, world);
        }
    }

    private List<Room> drawBentHallway(Room r1, Room r2, TETile[][] world) {
        Room topRoom;
        Room bottomRoom;
        if (r1.getYY() > r2.getYY()) {
            topRoom = r1;
            bottomRoom = r2;
        } else {
            topRoom = r2;
            bottomRoom = r1;
        }

        List<Room> newRooms = new ArrayList<>();
        int virtualX = bottomRoom.getXX() + (bottomRoom.getWidth() - 1) / 2;
        int virtualY = topRoom.getYY() + (topRoom.getHeight() - 1) / 2;

        Room virtualRoom = new Room(3, 3, virtualX, virtualY);
        drawRoom(virtualRoom, world);
        newRooms.add(virtualRoom);

        Room horizontalSection = drawHorizontalHallway(topRoom, virtualRoom, world);
        newRooms.add(horizontalSection);

        Room verticalSection = drawVerticalHallway(bottomRoom, virtualRoom, world);
        newRooms.add(verticalSection);

        return newRooms;
    }

    private Room drawVerticalHallway(Room r1, Room r2, TETile[][] world) {
        Room topRoom;
        Room bottomRoom;
        if (r1.getYY() > r2.getYY()) {
            topRoom = r1;
            bottomRoom = r2;
        } else {
            topRoom = r2;
            bottomRoom = r1;
        }
        for (int i = r1.getXX() + 1; i < r1.getXX() + r1.getWidth() - 1; i++) {
            if (i > r2.getXX() && i < (r2.getXX() + r2.getWidth() - 1)) {
                int h = 2 + topRoom.getYY() - (bottomRoom.getYY() + bottomRoom.getHeight());
                int w = 3;
                int x = i - 1;
                int y = bottomRoom.getYY() + bottomRoom.getHeight() - 1;
                Room hallway = new Room(h, w, x, y);
                drawRoom(hallway, world);
                hallway.addDoor(1, 0, world);
                hallway.addDoor(1, hallway.getHeight() - 1, world);
                return hallway;
            }
        }
        return null;
    }

    private Room drawHorizontalHallway(Room r1, Room r2, TETile[][] world) {
        Room rightRoom;
        Room leftRoom;
        if (r1.getXX() > r2.getXX()) {
            rightRoom = r1;
            leftRoom = r2;
        } else {
            rightRoom = r2;
            leftRoom = r1;
        }
        for (int i = r1.getYY() + 1; i < r1.getYY() + r1.getHeight() - 1; i++) {
            if (i > r2.getYY() && i < (r2.getYY() + r2.getHeight() - 1)) {
                int h = 3;
                int w = 2 + rightRoom.getXX() - (leftRoom.getXX() + leftRoom.getWidth());
                int x = leftRoom.getXX() + leftRoom.getWidth() - 1;
                int y = i - 1;
                Room hallway = new Room(h, w, x, y);
                drawRoom(hallway, world);
                hallway.addDoor(0, 1, world);
                hallway.addDoor(hallway.getWidth() - 1, 1, world);
                return hallway;
            }
        }
        return null;
    }

    private boolean verticallyAligned(Room r1, Room r2) {
        return r1.getXX() >= r2.getXX() && r1.getXX() < r2.getXX() + r2.getWidth() - 2
                || r2.getXX() >= r1.getXX() && r2.getXX() < r1.getXX() + r1.getWidth() - 2;
    }

    private boolean horizontallyAligned(Room r1, Room r2) {
        return r1.getYY() >= r2.getYY() && r1.getYY() < r2.getYY() + r2.getHeight() - 2
                || r2.getYY() >= r1.getYY() && r2.getYY() < r1.getYY() + r1.getHeight() - 2;
    }

    private void sortByDistTo(List<Room> rs, Room room) {
        Comparator<Room> c = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Room r1 = (Room) o1;
                Room r2 = (Room) o2;
                return (int) (r1.roughDist(room) - r2.roughDist(room));
            }
        };
        rs.sort(c);
    }

    private static void drawRoom(Room room, TETile[][] world) {
        int x = room.getXX();
        int y = room.getYY();
        int width = room.getWidth();
        int height = room.getHeight();
        //draw walls
        //bottom wall
        drawLineRight(x, y, width, world, Tileset.WALL);
        //top wall
        drawLineRight(x, y + height - 1, width, world, Tileset.WALL);
        //left wall
        drawLineUp(x, y + 1, height - 2, world, Tileset.WALL);
        //right wall
        drawLineUp(x + width - 1, y + 1, height - 2, world, Tileset.WALL);
        //draw floor
        for (int i = 0; i < height - 2; i++) {
            drawLineRight(x + 1, y + 1 + i, width - 2, world, Tileset.FLOOR);
        }
    }

    //@source: https://stackoverflow.com/questions/14825064/how-to-create-boolean-method
    private static boolean isOverlap(Room room, TETile[][] world) {
        int x = room.getXX();
        int y = room.getYY();
        int width = room.getWidth();
        int height = room.getHeight();

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                if (world[i][j].equals(Tileset.FLOOR)
                        || world[i][j].equals(Tileset.WALL)
                        || world[i][j].equals(Tileset.LOCKED_DOOR)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void drawLineRight(int x, int y, int l, TETile[][] world, TETile tile) {
        for (int i = 0; i < l; i++) {
            drawPixel(x + i, y, world, tile);
        }
    }

    private static void drawLineUp(int x, int y, int l, TETile[][] world, TETile tile) {
        for (int i = 0; i < l; i++) {
            drawPixel(x, y + i, world, tile);
        }
    }

    private static void drawPixel(int x, int y, TETile[][] world, TETile tile) {
        if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
            return;
        }
        if (tile == Tileset.WALL
                && (world[x][y] == Tileset.FLOOR || world[x][y] == Tileset.LOCKED_DOOR)) {
            return;
        }
        world[x][y] = tile;
    }
}

