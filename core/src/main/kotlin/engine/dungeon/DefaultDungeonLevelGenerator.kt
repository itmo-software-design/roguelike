package engine.dungeon

import engine.factory.MobManager
import vo.DungeonLevel

/**
 * Стандартная реализация DungeonLevelGenerator
 */
class DefaultDungeonLevelGenerator: DungeonLevelGenerator {
    /**
     * Генерирует уровни используя DungeonLevelRandomGenerator по указанным параметрам
     */
    override fun generate(levelCount: Int, height: Int, width: Int, roomCount: Int): List<DungeonLevel> {
        val levels = mutableListOf<DungeonLevel>()
        val generator = DungeonLevelRandomGenerator(height=height, width=width, roomCount=roomCount)
        for (level in 0 until levelCount) {
            levels.add(generator.generate())
        }
        return levels
    }

    /**
     * Загрузить уровни из файла. Сгенерировать рандомных мобов.
     * Кидает исключение, если тип источника LOAD_FROM_FILE и чтение не удалось.
     */
    override fun loadFromFile(filePath: String): List<DungeonLevel> {
        val levels = DungeonLevelFileLoader().load(filePath)
        levels.forEach { level ->
            MobManager.generateMobs(level)
        }
        return levels
    }
}
