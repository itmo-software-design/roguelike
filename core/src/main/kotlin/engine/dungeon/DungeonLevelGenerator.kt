package engine.dungeon

import vo.DungeonLevel

interface DungeonLevelGenerator {
    fun generate(levelCount: Int, height: Int, width: Int, roomCount: Int): List<DungeonLevel>
    fun loadFromFile(filePath: String): List<DungeonLevel>?
}

