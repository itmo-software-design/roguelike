package vo

import com.github.itmosoftwaredesign.roguelike.utils.vo.Position
import com.github.itmosoftwaredesign.roguelike.utils.vo.Renderable

class Level(
    val tiles: Array<Array<Tile>>,
    private val rooms: List<Room>
) {
    val startPosition = rooms.first().center
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
    HALL(blocked = false, blockSight = true),
    WALL(blocked = true, blockSight = true),
    DOOR(blocked = false, blockSight = true),
    GRASS(blocked = false, blockSight = true),
    WATER(blocked = true, blockSight = false),
    NONE(blocked = true, blockSight = false),

    CONSUMABLE(blocked = true, blockSight = false),
    WEAPON(blocked = true, blockSight = false),
    ARMOR(blocked = true, blockSight = false),

    MOB(blocked = true, blockSight = false),

    PORTAL(blocked = true, blockSight = false),
}

data class Tile(
    var type: TileType,
    var isExplored: Boolean = false
) : Renderable {
    override fun toString(): String {
        return when (type) {
            TileType.CONSUMABLE -> "c"
            TileType.WEAPON -> "w"
            TileType.ARMOR -> "a"

            TileType.MOB -> "X"

            TileType.FLOOR -> "."
            TileType.HALL -> "o"
            TileType.WALL -> "#"
            TileType.NONE -> " "
            TileType.DOOR -> "+"
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
class Room(
    val bottomLeft: Position,
    width: Int,
    height: Int
) {
    /**
     * Ширина комнаты
     *
     * Минимум 3 символа: стена, пол, стена
     */
    val width = width.coerceAtLeast(3)

    /**
     * Высота комнаты
     *
     * Минимум 3 символа: стена, пол, стена
     */
    val height = height.coerceAtLeast(3)

    /**
     * Правый верхний угол комнаты
     *
     * Полезно держать предварительно посчитанным для определения пересечения с другими комнатами
     */
    val topRight = Position(bottomLeft.x + width, bottomLeft.y + height)

    /**
     * Центр комнаты
     */
    val center = Position(
        x = bottomLeft.x + width / 2,
        y = bottomLeft.y + height / 2
    )

    /**
     * Евклидово расстояние до центра комнаты
     */
    val distanceFromZero = center.x * center.x + center.y * center.y

    /**
     * Определяет, пересекаются ли границы текущей комнаты с другой
     */
    fun intersects(other: Room): Boolean {
        return bottomLeft.x <= other.topRight.x
                && topRight.x >= other.bottomLeft.x
                && bottomLeft.y <= other.topRight.y
                && topRight.y >= other.bottomLeft.y
    }
}
