package engine

import engine.dungeon.DungeonLevelBuilder
import engine.dungeon.DungeonLevelDirector
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import vo.DungeonLevel
import vo.Inventory
import vo.Player
import java.nio.file.Path

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Serializable
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
    lateinit var dungeonLevels: List<DungeonLevel>
        private set

    /**
     * Текущий уровень
     */
    lateinit var currentDungeonLevel: DungeonLevel
        private set

    private const val SAVED_MAPS_FOLDER_NAME = "maps"
    internal const val DUNGEON_LEVELS_COUNT = 3
    var currentDungeonLevelNum = 0
        private set

    /**
     * Создает нового игрока и генерирует уровни.
     * Если пользователь ввел имя файла - пытаемся загрузить карту из файла.
     */
    fun startNewGame(playerName: String, fileName: String = "") {
        val director = DungeonLevelDirector()
        val builder = DungeonLevelBuilder()
        if (fileName.isEmpty()) {
            director.constructRandomLevels(builder)
        } else {
            director.loadLevelsFromFile(builder, Path.of(SAVED_MAPS_FOLDER_NAME, fileName))
        }

        try {
            dungeonLevels = builder.build()
        } catch (e: Exception) {
            // TODO: в идеале при ошибке чтения из файла, нужно давать возможность ввести новый, пока просто загружаем рандомную карту
            logger.info { "error while loading map from file: $e" }
            director.constructRandomLevels(builder)
            dungeonLevels = builder.build()
        }

        val firstDungeonLevel = dungeonLevels.first()
        player = Player(playerName, 100, 10, 1, firstDungeonLevel.startPosition)
        currentDungeonLevelNum = 0
        currentDungeonLevel = firstDungeonLevel
    }

    /**
     * Обновляет текущий уровень, позицию игрока и инвентарь.
     */
    fun moveToNextLevel() {
        currentDungeonLevelNum = (currentDungeonLevelNum + 1) % DUNGEON_LEVELS_COUNT // TODO: show 'Game Finished' plane
        currentDungeonLevel = dungeonLevels[currentDungeonLevelNum]
        player.position = currentDungeonLevel.startPosition
        player.inventory = Inventory() // TODO: Should inventory be taken over?
    }

    private val logger = KotlinLogging.logger {}
}
