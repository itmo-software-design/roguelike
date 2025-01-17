package engine

import com.github.itmosoftwaredesign.roguelike.utils.vo.Inventory
import com.github.itmosoftwaredesign.roguelike.utils.vo.Player
import java.nio.file.Path
import vo.Level

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
object GameSession {

    @Deprecated(message = "Переехать на Player")
    lateinit var playerName: String

    /**
     * Персонаж игрока
     */
    lateinit var player: Player
        private set

    /**
     * Список уровней
     */
    lateinit var levels: MutableList<Level>
        private set

    /**
     * Текущий уровень
     */
    lateinit var currentLevel: Level
        private set

    private val levelsCount = 3
    private var currentLevelId = 0

    /**
     * Создает нового игрока и генерирует уровни.
     */
    fun startNewGame(playerName: String, firstLevel: Level) {
        player = Player(playerName, 100, 1, 1, firstLevel.startPosition)
        levels = mutableListOf(firstLevel)
        addMoreLevels()
        currentLevel = firstLevel
    }

    /**
     * Обновляет текущий уровень, позицию игрока и инвентарь.
     */
    fun moveToNextLevel() {
        currentLevelId = (currentLevelId + 1) % levelsCount // TODO: show 'Game Finished' plane
        currentLevel = levels[currentLevelId]
        player.position = currentLevel.startPosition
        player.inventory = Inventory()
    }

    private fun addMoreLevels() {
        for (level in 0 until levelsCount) {
            levels.add(LevelGenerator(42 + level + 1).generate())
        }
    }

    fun loadStateFromFile(filePath: Path) {
        TODO("Загрузка состояния игры из файла")
    }
}
