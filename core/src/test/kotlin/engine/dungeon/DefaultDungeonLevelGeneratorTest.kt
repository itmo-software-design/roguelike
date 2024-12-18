package engine.dungeon

import kotlin.test.BeforeTest
import kotlin.test.Test

class DefaultDungeonLevelGeneratorTest {

    private lateinit var generator: DefaultDungeonLevelGenerator

    @BeforeTest
    fun setUp() {
        generator = DefaultDungeonLevelGenerator()
    }

    @Test
    fun `should generate random levels`() {
        // given
        val levelCount = 5
        val height = 10
        val width = 20
        val roomCount = 3

        // when
        val levels = generator.generate(levelCount, height, width, roomCount)

        // then
        assert(levels.count() == levelCount)
        for (level in levels) {
            assert(level.height == height)
            assert(level.width == width)
            assert(level.rooms.count() >= 1 && level.rooms.count() <= roomCount)
        }
    }

    @Test
    fun `should load levels from correct file and add random mobs`() {
        // given
        val fileName = javaClass.classLoader.getResource("maps/correctMap.json")!!.path.toString()

        // when
        val levels = generator.loadFromFile(fileName)

        // then
        val mobsCount = levels.map { it.enemies.count() }.reduce { acc, i -> acc + i }
        assert(mobsCount > 0)
        assert(levels.count() == 3)
    }
}
