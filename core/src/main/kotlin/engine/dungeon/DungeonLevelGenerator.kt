package engine.dungeon

import engine.factory.FirstLevelMobFactory
import engine.factory.MobFactory
import engine.factory.MobManager
import kotlinx.serialization.json.Json
import vo.DungeonLevel
import vo.Room
import vo.Tile
import java.io.File

class DungeonLevelGenerator {
    class DungeonLevelBuilder internal constructor() {
        private var tiles: Array<Array<Tile>>? = null
        private var rooms: List<Room>? = null
        private var mobFactory: MobFactory = FirstLevelMobFactory

        internal fun tiles(tiles: Array<Array<Tile>>): DungeonLevelBuilder {
            this.tiles = tiles
            return this
        }

        internal fun rooms(rooms: List<Room>): DungeonLevelBuilder {
            this.rooms = rooms
            return this
        }

        fun mobFactory(mobFactory: MobFactory): DungeonLevelBuilder {
            this.mobFactory = mobFactory
            return this
        }

        fun build(): DungeonLevel {
            val levelTiles = tiles
                ?: throw IllegalStateException("tiles must be set before build()")
            val levelRooms = rooms
                ?: throw IllegalStateException("rooms must be set before build()")
            val level = DungeonLevel(
                tiles = levelTiles,
                rooms = levelRooms
            )
            MobManager.generateMobs(mobFactory, level)
            return level
        }
    }

    /**
     * Строитель рандомного уровня
     */
    class DungeonLevelGeneratorRandomBuilder internal constructor() {
        private var seed: Int = 42
        private var mobFactory: MobFactory = FirstLevelMobFactory
        private var height: Int = 30
        private var width: Int = 50
        private var roomCount: Int = 5
        private var roomMinCount: Int? = null
        private var roomMaxCount: Int? = null
        private var roomMinSize: Int = 5
        private var roomMaxSize: Int = 10

        fun seed(value: Int): DungeonLevelGeneratorRandomBuilder {
            this.seed = value
            return this
        }

        fun mobFactory(value: MobFactory): DungeonLevelGeneratorRandomBuilder {
            this.mobFactory = value
            return this
        }

        fun height(value: Int): DungeonLevelGeneratorRandomBuilder {
            this.height = value
            return this
        }

        fun width(value: Int): DungeonLevelGeneratorRandomBuilder {
            this.width = value
            return this
        }

        fun roomCount(value: Int): DungeonLevelGeneratorRandomBuilder {
            this.roomCount = value
            return this
        }

        fun roomMinCount(value: Int?): DungeonLevelGeneratorRandomBuilder {
            this.roomMinCount = value
            return this
        }

        fun roomMaxCount(value: Int?): DungeonLevelGeneratorRandomBuilder {
            this.roomMaxCount = value
            return this
        }

        fun roomMinSize(value: Int): DungeonLevelGeneratorRandomBuilder {
            this.roomMinSize = value
            return this
        }

        fun roomMaxSize(value: Int): DungeonLevelGeneratorRandomBuilder {
            this.roomMaxSize = value
            return this
        }

        fun toLevelBuilder(): DungeonLevelBuilder {
            val generator = DungeonLevelRandomGenerator(
                seed = seed,
                height = height,
                width = width,
                roomCount = roomCount,
                roomMinCount = roomMinCount,
                roomMaxCount = roomMaxCount,
                roomMinSize = roomMinSize,
                roomMaxSize = roomMaxSize
            )
            val dungeonLevel = generator.generate()
            return DungeonLevelBuilder()
                .tiles(dungeonLevel.tiles)
                .rooms(dungeonLevel.rooms)
                .mobFactory(mobFactory)
        }
    }

    /**
     * Строитель уровня, взятого из файла
     */
    class DungeonLevelGeneratorFileBuilder internal constructor() {
        private var mobFactory: MobFactory = FirstLevelMobFactory

        fun mobFactory(value: MobFactory): DungeonLevelGeneratorFileBuilder {
            this.mobFactory = value
            return this
        }

        fun file(path: String): DungeonLevelBuilder {
            val resourcePath = this.javaClass.classLoader.getResource(path)?.file
                ?: throw RuntimeException("No resource $path found to load")
            val jsonContent = File(resourcePath).readText()
            val loadedDungeonLevel = Json.decodeFromString<DungeonLevel>(jsonContent)

            return DungeonLevelBuilder()
                .tiles(loadedDungeonLevel.tiles)
                .rooms(loadedDungeonLevel.rooms)
        }
    }

    companion object {
        fun randomBuilder(): DungeonLevelGeneratorRandomBuilder {
            return DungeonLevelGeneratorRandomBuilder()
        }

        fun fileBuilder(): DungeonLevelGeneratorFileBuilder {
            return DungeonLevelGeneratorFileBuilder()
        }
    }
}
