package vo

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Уровень подземелья
 *
 * @author MikhailShad
 * @since 0.0.1
 */
@Serializable
class DungeonLevel(
    val tiles: Array<Array<Tile>>,
    val rooms: List<Room>,
    @Transient
    val enemies: MutableList<Mob> = mutableListOf()
) {
    /**
     * Ширина уровня
     */
    val width = tiles.size

    /**
     * Высота уровня
     */
    val height = tiles[0].size

    /**
     * Стартовая позиция [Player] на уровне
     */
    val startPosition = rooms.first().center

    /**
     * Возвращает тайл уровня в указанной точке
     */
    fun getTileAt(position: Position): Tile {
        return tiles[position.x][position.y]
    }

    /**
     * Проверяет, что точка принадлежит уровню
     */
    fun isInBounds(position: Position): Boolean {
        return position.x in 0 until width && position.y in 0 until height
    }
}

/**
 * Тип тайла на карте уровня
 *
 * @author MikhailShad
 * @since 0.0.1
 */
enum class TileType(
    /**
     * Блокирует ли проход
     */
    val blocked: Boolean,
    /**
     * Блокирует ли обзор
     */
    val blockSight: Boolean,
) {
    FLOOR(blocked = false, blockSight = false),
    HALL(blocked = false, blockSight = false),
    WALL(blocked = true, blockSight = true),
    DOOR_CLOSED(blocked = false, blockSight = true),
    DOOR_OPENED(blocked = false, blockSight = false),
    GRASS(blocked = false, blockSight = true),
    WATER(blocked = true, blockSight = false),
    NONE(blocked = true, blockSight = false),

    CONSUMABLE(blocked = true, blockSight = false),
    WEAPON(blocked = true, blockSight = false),
    ARMOR(blocked = true, blockSight = false),

    PORTAL(blocked = true, blockSight = false),
}

@Serializable
data class Tile(
    var type: TileType,
    var isExplored: Boolean = false
) {
    override fun toString(): String {
        return when (type) {
            TileType.CONSUMABLE -> "c"
            TileType.WEAPON -> "w"
            TileType.ARMOR -> "a"
            TileType.FLOOR -> "."
            TileType.HALL -> "o"
            TileType.WALL -> "#"
            TileType.NONE -> " "
            TileType.DOOR_CLOSED -> "+"
            TileType.DOOR_OPENED -> "_"
            TileType.PORTAL -> "0"
            TileType.GRASS -> "|"
            TileType.WATER -> "~"
        }
    }
}

/**
 * Комната уровня
 *
 * @author MikhailShad
 * @since 0.0.1
 */
@Serializable
class Room(
    val bottomLeft: Position,
    val rawWidth: Int,
    val rawHeight: Int
) {
    /**
     * Ширина комнаты
     *
     * Минимум 3 символа: стена, пол, стена
     */
    val width = rawWidth.coerceAtLeast(3)

    /**
     * Высота комнаты
     *
     * Минимум 3 символа: стена, пол, стена
     */
    val height = rawHeight.coerceAtLeast(3)

    /**
     * Правый верхний угол комнаты
     *
     * Полезно держать предварительно посчитанным для определения пересечения с другими комнатами
     */
    @Transient
    val topRight = Position(bottomLeft.x + width, bottomLeft.y + height)

    /**
     * Центр комнаты
     */
    @Transient
    val center = Position(
        x = bottomLeft.x + width / 2,
        y = bottomLeft.y + height / 2
    )

    /**
     * Евклидово расстояние до центра комнаты
     */
    @Transient
    val distanceFromZero = center.distanceToZero

    /**
     * Определяет, пересекаются ли границы текущей комнаты с другой
     */
    fun intersects(other: Room): Boolean {
        return bottomLeft.x <= other.topRight.x
                && topRight.x >= other.bottomLeft.x
                && bottomLeft.y <= other.topRight.y
                && topRight.y >= other.bottomLeft.y
    }

    /**
     * Проверяет, что позиция находится внутри комнаты
     */
    fun isInside(position: Position): Boolean {
        return position.x in bottomLeft.x..topRight.x
                && position.y in bottomLeft.y..topRight.y
    }
}
