package engine

import kotlin.test.Test
import kotlin.test.assertNotNull

class DungeonDungeonLevelGeneratorTest {
    @Test
    fun generate() {
        val dungeonLevelGenerator = DungeonLevelGenerator(
            seed = 42,
            height = 20,
            width = 20,
            roomCount = 3,
            roomMinSize = 3,
            roomMaxSize = 5
        )
        val level = dungeonLevelGenerator.generate()
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
