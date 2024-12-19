package engine

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GameSessionTest {

    @Test
    fun `should init necessary fields when starting new game`() {
        // given
        val playerName = "playerName"

        // when
        GameSession.startNewGame(playerName)

        // then
        assert(GameSession.player.name == playerName)
        assert(GameSession.dungeonLevels.isNotEmpty())
        assert(GameSession.currentDungeonLevel == GameSession.dungeonLevels.first())
        assert(GameSession.currentDungeonLevelNum == 0)
    }

    @Test
    fun `should move to next levels`() {
        // given
        GameSession.startNewGame("playerName")

        // when/then
        for (levelNum in 1 until GameSession.DUNGEON_LEVELS_COUNT) {
            GameSession.moveToNextLevel()

            assert(GameSession.currentDungeonLevelNum == levelNum)
            assert(GameSession.currentDungeonLevel == GameSession.dungeonLevels[levelNum])
            assert(GameSession.player.position == GameSession.dungeonLevels[levelNum].startPosition)
        }

        assertThrows<RuntimeException> { GameSession.moveToNextLevel() }
    }
}
