package vo

import kotlin.random.Random

object Randomizer {
    private val random = Random(42)

    fun nextInt(until: Int): Int {
        return random.nextInt(until)
    }

    fun nextInt(from: Int, until: Int): Int {
        return random.nextInt(from, until)
    }

    fun nextBoolean(): Boolean {
        return random.nextBoolean()
    }

    /**
     * Сгенерировать рандомную позицию
     * x1 <= x < x2
     * y1 <= y < y2
     */
    fun generateRandomPosition(x1: Int, x2: Int, y1: Int, y2: Int): Position {
        val x = random.nextInt(x1, x2)
        val y = random.nextInt(y1, y2)
        return Position(x, y)
    }
}
