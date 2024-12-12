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

    /**
     * Соседние позиции с текущей
     *
     * Могут выходить за границы карты
     */
    val neighbours
        get() = listOf(
            copy(x = x + 1),
            copy(x = x - 1),
            copy(y = y + 1),
            copy(y = y - 1)
        )

    companion object {
        val ZERO = Position(0, 0)
    }
}
