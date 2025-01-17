package engine

import engine.dungeon.DungeonLevelGenerator
import engine.factory.BossFactory
import engine.factory.FirstLevelMobFactory
import engine.factory.SecondLevelMobFactory
import kotlinx.serialization.Serializable
import vo.DungeonLevel
import vo.Player

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
    lateinit var dungeonLevels: MutableList<DungeonLevel>
        private set

    /**
     * Текущий уровень
     */
    lateinit var currentDungeonLevel: DungeonLevel
        private set

    /**
     * Ядро генерации псевдослучайных чисел
     */
    var seed: Int = 42

    private const val BOSS_LEVEL_FILE_PATH = "levels/boss_level.json"
    internal const val DUNGEON_LEVELS_COUNT = 3
    private const val BOSS_LEVEL_NUM = DUNGEON_LEVELS_COUNT - 1
    var currentDungeonLevelNum = 0
        private set

    /**
     * Создает нового игрока и генерирует первый уровень
     */
    fun startNewGame(playerName: String) {
        seed = playerName.hashCode()
        val firstDungeonLevel = DungeonLevelGenerator
            .randomBuilder()
            .seed(seed)
            .mobFactory(FirstLevelMobFactory)
            .height(30)
            .width(50)
            .roomCount(5)
            .toLevelBuilder()
            .build()

        dungeonLevels = mutableListOf(firstDungeonLevel)
        currentDungeonLevel = firstDungeonLevel
        currentDungeonLevelNum = 0

        player = Player(playerName, 100, 10, 1, firstDungeonLevel.startPosition)
    }

    /**
     * Генерирует следующий уровень и переносит на него игрока
     */
    fun moveToNextLevel() {
        if (++currentDungeonLevelNum == DUNGEON_LEVELS_COUNT) {
            throw RuntimeException("Reached the deepest dungeon level")
        }

        val nextLevel = if (currentDungeonLevelNum == BOSS_LEVEL_NUM) {
            DungeonLevelGenerator.fileBuilder()
                .file(BOSS_LEVEL_FILE_PATH)
                .mobLimit(1)
                .mobFactory(BossFactory)
                .build()
        } else {
            DungeonLevelGenerator
                .randomBuilder()
                .seed(seed)
                .mobFactory(SecondLevelMobFactory)
                .height(30)
                .width(50)
                .roomCount(5)
                .toLevelBuilder()
                .build()
        }

        dungeonLevels.add(nextLevel)
        currentDungeonLevel = nextLevel
        player.position = nextLevel.startPosition
    }
}
