package engine

import com.github.itmosoftwaredesign.roguelike.utils.vo.Inventory
import vo.DungeonLevel
import vo.Player
import java.nio.file.Path

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
object GameSession {

    /**
     * Персонаж игрока
     */
    lateinit var player: Player
        private set

    /**
     * Признак того, что игрок инициализирован
     */
    fun isPlayerInitialized() = ::player.isInitialized

    /**
     * Список уровней
     */
    lateinit var dungeonLevels: MutableList<DungeonLevel>
        private set

    /**
     * Текущий уровень
     */
    lateinit var currentDungeonLevel: DungeonLevel
        private set

    private val levelsCount = 3
    private var currentLevelId = 0

    /**
     * Создает нового игрока и генерирует уровни.
     */
    fun startNewGame(playerName: String) {
        val randomSeed = playerName.hashCode()
        val firstDungeonLevel = DungeonLevelGenerator(randomSeed).generate()
        player = Player(playerName, 100, 10, 1, firstDungeonLevel.startPosition)
        dungeonLevels = mutableListOf(firstDungeonLevel)
        addMoreLevels()
        currentDungeonLevel = firstDungeonLevel
    }

    /**
     * Обновляет текущий уровень, позицию игрока и инвентарь.
     */
    fun moveToNextLevel() {
        currentLevelId = (currentLevelId + 1) % levelsCount // TODO: show 'Game Finished' plane
        currentDungeonLevel = dungeonLevels[currentLevelId]
        player.position = currentDungeonLevel.startPosition
        player.inventory = Inventory()
    }

    private fun addMoreLevels() {
        for (level in 0 until levelsCount) {
            dungeonLevels.add(DungeonLevelGenerator(42 + level + 1).generate())
        }
    }

    fun loadStateFromFile(filePath: Path) {
        TODO("Загрузка состояния игры из файла")
    }
}
