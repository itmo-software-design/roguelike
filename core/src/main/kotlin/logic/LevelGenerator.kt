package logic

import com.github.itmosoftwaredesign.roguelike.utils.vo.*
import java.util.*
import kotlin.random.Random

class LevelGenerator(
    seed: Int,
    private val width: Int = 50,
    private val height: Int = 30,
    roomCount: Int = 5,
    roomMinCount: Int? = null,
    roomMaxCount: Int? = null,
    private val roomMinSize: Int = 5,
    private val roomMaxSize: Int = 10
) {

    private val randomizer = Random(seed)
    private val roomCount = if (roomMinCount != null && roomMaxCount != null) {
        randomizer.nextInt(
            roomMinCount.coerceAtLeast(1),
            roomMaxCount.coerceAtLeast(2)
        )
    } else {
        roomCount
    }
    private val tiles: Array<Array<Tile>> = initializeTiles()


    private fun initializeTiles(): Array<Array<Tile>> {
        return Array(width) { Array(height) { Tile(TileType.NONE) } }
    }

    fun generate(): Level {
        val rooms: MutableList<Room> = mutableListOf()
        for (i in 1..roomCount) {
            val maxRetries = 5
            var retryCount = 0
            var room: Room
            var hasIntersections: Boolean
            do {
                room = generateRandomRoom()
                ++retryCount
                hasIntersections = rooms.any { it.intersects(room) }
            } while (hasIntersections && retryCount < maxRetries)

            if (hasIntersections) {
                break // Создать новую комнату так и не вышло, выходим из цикла генерации
            }

            // Создать комнату получилось, нарисуем её
            carveRoom(room)
            rooms.add(room)
        }

        connectRooms(rooms)

        return Level(width, height, tiles, rooms as List<Room>)
    }

    private fun generateRandomRoom(): Room {
        val roomWidth = randomizer.nextInt(roomMinSize, roomMaxSize)
        val roomHeight = randomizer.nextInt(roomMinSize, roomMaxSize)
        val x = randomizer.nextInt(0, width - roomWidth)
        val y = randomizer.nextInt(0, height - roomHeight)
        return Room(Position(x, y), roomWidth, roomHeight)
    }

    private fun carveRoom(room: Room) {
        // Проставим горизонтальные стены
        for (x in room.bottomLeft.x..room.topRight.x) {
            tiles[x][room.bottomLeft.y].type = TileType.WALL
            tiles[x][room.topRight.y].type = TileType.WALL
        }

        // Проставим вертикальные стены
        for (y in room.bottomLeft.y..room.topRight.y) {
            tiles[room.bottomLeft.x][y].type = TileType.WALL
            tiles[room.topRight.x][y].type = TileType.WALL
        }

        // Проставим пол
        for (x in room.bottomLeft.x + 1 until room.topRight.x) {
            for (y in room.bottomLeft.y + 1 until room.topRight.y) {
                tiles[x][y].type = TileType.FLOOR
            }
        }
    }

    private fun connectRooms(rooms: List<Room>) {
        val roomsQueue = PriorityQueue<Room>(rooms.size) { a, b -> a.distanceFromZero - b.distanceFromZero }
        roomsQueue.addAll(rooms)

        val first = roomsQueue.poll()
        var current = first
        while (roomsQueue.isNotEmpty()) {
            val next = roomsQueue.poll()
            connectRooms(current, next)
            current = next
        }
        connectRooms(current, first)
    }

    private fun connectRooms(room1: Room, room2: Room) {
        val center1 = room1.center
        val center2 = room2.center

        if (randomizer.nextBoolean()) {
            carveHorizontalTunnelWithDoors(center1.x, center2.x, center1.y)
            carveVerticalTunnelWithDoors(center1.y, center2.y, center2.x)
        } else {
            carveVerticalTunnelWithDoors(center1.y, center2.y, center1.x)
            carveHorizontalTunnelWithDoors(center1.x, center2.x, center2.y)
        }
    }

    private fun carveHorizontalTunnelWithDoors(x1: Int, x2: Int, y: Int) {
        for (x in minOf(x1, x2)..maxOf(x1, x2)) {
            val tile = tiles[x][y]
            if (tile.type == TileType.WALL) {
                tile.type = TileType.DOOR_CLOSED
            } else if (tile.type == TileType.NONE) {
                tile.type = TileType.HALL
            }
        }
    }

    private fun carveVerticalTunnelWithDoors(y1: Int, y2: Int, x: Int) {
        for (y in minOf(y1, y2)..maxOf(y1, y2)) {
            val tile = tiles[x][y]
            if (tile.type == TileType.WALL) {
                tile.type = TileType.DOOR_CLOSED
            } else if (tile.type == TileType.NONE) {
                tile.type = TileType.HALL
            }
        }
    }
}
