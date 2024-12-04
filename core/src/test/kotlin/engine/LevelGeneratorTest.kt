package engine

import kotlin.test.Test
import kotlin.test.assertNotNull

class LevelGeneratorTest {
    @Test
    fun generate() {
        val levelGenerator = LevelGenerator(
            seed = 42,
            width = 20,
            height = 20,
            roomCount = 3,
            roomMinSize = 3,
            roomMaxSize = 5
        )
        val level = levelGenerator.generate()
        assertNotNull(level)

        val sb = StringBuilder()
        level.tiles.forEach {
            it.forEach {
                sb.append(
                    it.toString()
                )
            }
            sb.append('\n')
        }
        println(sb.toString())
    }
}
