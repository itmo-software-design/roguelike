package engine

import com.github.itmosoftwaredesign.roguelike.utils.vo.Level
import com.github.itmosoftwaredesign.roguelike.utils.vo.Player
import java.nio.file.Path

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

    fun startNewGame(playerName: String, firstLevel: Level) {
        player = Player(playerName, 100, 1, 1, firstLevel.startPosition)
        levels = mutableListOf(firstLevel)
        currentLevel = firstLevel
    }

    fun loadStateFromFile(filePath: Path) {
        TODO("Загрузка состояния игры из файла")
    }
}
