# Build Your Own World Design Document

**Partner 1:**
Gyeongmin Lee

**Partner 2:**
Jordan Heinemann

## Classes and Data Structures
Position: x axis and y axis to indicate rooms' location

Room: define rooms' height and weight, position

MapGenerator:
1. initialize tile
2. initialize rooms and hallways. (number of rooms and hallways are random and each size is also random.
3. Also, we think that hallways as same as room, but it has width=1 or  height =1(We devide as horizental hallways and vertical hallways.)
4. I put " isOverlap" , because, room and room cannot be connected each other.
5. I applied that "isOverlap" method only when we make rooms. (Hallways can be overlaped and intersected)  
6. Add drawDoor method for each rooms and hallways. (Random door)
7. Make DrawPath method to sConnect (room-room & room-hallway & hallway-hallway)

MapGeneratorTest: just testing file for MapGenerator

## Algorithms
We did not use any specific algorithm yet.

## Persistence
