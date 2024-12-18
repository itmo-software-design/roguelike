package engine.dungeon

import vo.DungeonLevel

class DungeonLevelBuilder(private val generator: DefaultDungeonLevelGenerator = DefaultDungeonLevelGenerator()) {
    private var levelCount: Int = 0
    private var height: Int = 0
    private var width: Int = 0
    private var roomCount: Int = 0
    private var sourceType: SourceType = SourceType.GENERATE
    private var filePath: String = ""

    private enum class SourceType {
        GENERATE,
        LOAD_FROM_FILE
    }

    /**
     * Установить количество уровней
     */
    fun setLevelCount(count: Int): DungeonLevelBuilder {
        this.levelCount = count
        return this
    }

    /**
     * Установить размеры карты
     */
    fun setDimensions(height: Int, width: Int): DungeonLevelBuilder {
        this.height = height
        this.width = width
        return this
    }

    /**
     * Установить количество комнат
     */
    fun setRoomCount(count: Int): DungeonLevelBuilder {
        this.roomCount = count
        return this
    }

    /**
     * Установить путь к файлу и источник карты - LOAD_FROM_FILE
     */
    fun loadFromFile(filePath: String): DungeonLevelBuilder {
        this.sourceType = SourceType.LOAD_FROM_FILE
        this.filePath = filePath
        return this
    }

    /**
     * Установить тип источника карты - GENERATE
     */
    fun generate(): DungeonLevelBuilder {
        this.sourceType = SourceType.GENERATE
        return this
    }

    /**
     * На основании указанных параметров и типа источника карты получить уровни
     * Кидает исключение, если тип источника LOAD_FROM_FILE и чтение не удалось.
     */
    fun build(): List<DungeonLevel> {
        return when (sourceType) {
            SourceType.GENERATE -> generator.generate(levelCount, height, width, roomCount)
            SourceType.LOAD_FROM_FILE -> generator.loadFromFile(filePath)
        }
    }
}


