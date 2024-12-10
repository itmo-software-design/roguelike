package vo

import kotlin.math.abs

/**
 * Положение объекта на карте уровня
 *
 * @since MikhailShad
 * @since 0.0.1
 */
data class Position(var x: Int, var y: Int) {
    /**
     * Квадрат расстояния от текущей точки до другой точки на уровне
     */
    fun euclideanDistanceTo(other: Position): Int {
        val deltaX = x - other.x
        val deltaY = y - other.y
        return deltaX * deltaX + deltaY * deltaY
    }

    /**
     * Манхеттенское расстояния от текущей точки до другой точки на уровне
     */
    fun manhattanDistanceTo(other: Position): Int {
        val deltaX = x - other.x
        val deltaY = y - other.y
        return abs(deltaX) + abs(deltaY)
    }

    /**
     * Квадрат расстояния от начала координат до текущей точки на уровне
     */
    val distanceToZero get() = euclideanDistanceTo(ZERO)

    companion object {
        val ZERO = Position(0, 0)
    }
}
