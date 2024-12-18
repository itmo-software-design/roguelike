package vo

import kotlin.test.Test

class RandomizerTest {

    @Test
    fun `should generate random position`() {
        val pos = Randomizer.generateRandomPosition(0, 10, 20, 30)
        assert(pos.x >= 0 && pos.x < 10)
        assert(pos.y >= 20 && pos.y < 30)
    }
}
