package engine

import kotlin.random.Random

/**
 * Генератор случайных чисел
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object Randomizer {
    lateinit var random: Random
        private set

    fun seed(seed: Int): Random {
        random = Random(System.currentTimeMillis() + seed)
        return random
    }
}
