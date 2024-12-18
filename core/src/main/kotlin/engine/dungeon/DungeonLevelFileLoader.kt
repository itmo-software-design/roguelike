package engine.dungeon

import kotlinx.serialization.json.Json
import vo.DungeonLevel
import java.io.File

class DungeonLevelFileLoader {
    /**
     * Загрузить уровни из файла по пути filePath
     * Кидает исключение, если тип источника LOAD_FROM_FILE и чтение не удалось.
     */
    fun load(filePath: String): List<DungeonLevel> {
        val json = File(filePath).readText()
        val decodedLevels = Json.decodeFromString<List<DungeonLevel>>(json)
        return decodedLevels
    }
}
