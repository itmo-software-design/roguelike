package engine.dungeon

import engine.GameSession.DUNGEON_LEVELS_COUNT
import java.nio.file.Path

/**
 * Класс по созданию объектов
 */
class DungeonLevelDirector {
    /**
     * Сконструировать уровни рандомно
     */
    fun constructRandomLevels(builder: DungeonLevelBuilder) {
        builder.setLevelCount(DUNGEON_LEVELS_COUNT)
        builder.setDimensions(30, 50)
        builder.setRoomCount(5)
        builder.generate()
    }

    /**
     * Загрузить уровни из файла
     * Бросает исключение, если произошла ошибка чтения/сериализации
     */
    fun loadLevelsFromFile(builder: DungeonLevelBuilder, filePath: Path) {
        builder.loadFromFile(filePath.toString())
    }
}
