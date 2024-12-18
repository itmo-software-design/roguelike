package engine.dungeon

import engine.factory.FirstLevelMobFactory
import kotlin.test.Test

class DefaultDungeonLevelGeneratorTest {

    @Test
    fun `should generate random level`() {
        // given
        val height = 10
        val width = 20
        val roomCount = 3

        // when
        val level = DungeonLevelGenerator
            .randomBuilder()
            .width(width)
            .height(height)
            .roomCount(roomCount)
            .generate()
            .build()

        // then
        assert(level.height == height)
        assert(level.width == width)
        assert(level.rooms.count() >= 1 && level.rooms.count() <= roomCount)
    }

    @Test
    fun `should load level from file and add random mobs`() {
        // given
        val filePath = javaClass.classLoader.getResource("levels/boss_level.json")!!.path.toString()

        // when
        val level = DungeonLevelGenerator
            .fileBuilder()
            .mobFactory(FirstLevelMobFactory)
            .file(filePath)
            .build()

        // then
        assert(level.enemies.count() > 0)
    }
}
