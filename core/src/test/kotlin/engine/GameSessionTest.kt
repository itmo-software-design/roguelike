package engine

import org.junit.jupiter.api.Test

class GameSessionTest {

    @Test
    fun `should init necessary fields when starting new game`() {
        // given
        val playerName = "playerName"

        // when
        GameSession.startNewGame(playerName)

        // then
        assert(GameSession.player.name == playerName)
        assert(GameSession.dungeonLevels.count() > 0)
        assert(GameSession.currentDungeonLevel == GameSession.dungeonLevels.first())
        assert(GameSession.currentDungeonLevelNum == 0)
    }

    @Test
    fun `should move to next levels`() {
        // given
        GameSession.startNewGame("playerName")

        // when/then
        for (level in 0..GameSession.DUNGEON_LEVELS_COUNT) {
            GameSession.moveToNextLevel()
            val levelNum = (level + 1) % GameSession.DUNGEON_LEVELS_COUNT
            assert(GameSession.currentDungeonLevelNum == levelNum)
            assert(GameSession.currentDungeonLevel == GameSession.dungeonLevels[levelNum])
            assert(GameSession.player.position == GameSession.dungeonLevels[levelNum].startPosition)
            assert(GameSession.player.inventory.count() == 0)
        }
    }
}
