package engine

import engine.dungeon.DungeonLevelGenerator
import engine.factory.FirstLevelMobFactory
import engine.factory.SecondLevelMobFactory
import kotlinx.serialization.Serializable
import vo.DungeonLevel
import vo.Inventory
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
    var dungeonLevels = mutableListOf<DungeonLevel>()
        private set

    /**
     * Текущий уровень
     */
    lateinit var currentDungeonLevel: DungeonLevel
        private set

    private const val BOSS_LEVEL_FILE_PATH = "levels/boss_level.json"
    internal const val DUNGEON_LEVELS_COUNT = 3
    private const val BOSS_LEVEL_NUM = DUNGEON_LEVELS_COUNT - 1
    var currentDungeonLevelNum = 0
        private set

    /**
     * Создает нового игрока и генерирует уровни.
     * Если пользователь ввел имя файла - пытаемся загрузить карту из файла.
     */
    fun startNewGame(playerName: String) {
        generateLevels()
        val firstDungeonLevel = dungeonLevels.first()

        player = Player(playerName, 100, 10, 1, firstDungeonLevel.startPosition)
        currentDungeonLevelNum = 0
        currentDungeonLevel = firstDungeonLevel
    }

    private fun generateLevels() {
        for (level in 0 until DUNGEON_LEVELS_COUNT) {
            val mobFactory = when (level) {
                0 -> FirstLevelMobFactory
                BOSS_LEVEL_NUM -> SecondLevelMobFactory /* Can be special BossFactory */
                else -> SecondLevelMobFactory
            }

            // TODO: We can load level from file using DungeonLevelGenerator.fileBuilder() if needed (e.x.: for BOSS level)
            val dungeonLevel = DungeonLevelGenerator
                .randomBuilder()
                .seed(42 + level + 1)
                .mobFactory(mobFactory)
                .height(30)
                .width(50)
                .roomCount(5)
                .generate()
                .build()

            dungeonLevels.add(dungeonLevel)
        }
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
}
