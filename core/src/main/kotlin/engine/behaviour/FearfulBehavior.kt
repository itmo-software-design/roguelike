package engine.behaviour

import vo.DungeonLevel
import vo.Mob
import vo.Player
import vo.Position


/**
 * Пугливое поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
class FearfulBehavior : Behavior {
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (isPlayerVisible(mob.position, player.position, dungeonLevel)) {
            moveAway(mob, player.position, dungeonLevel)
        }
    }

    private fun isPlayerVisible(
        mobPosition: Position,
        playerPosition: Position,
        dungeonLevel: DungeonLevel
    ): Boolean {
        // TODO Логика определения видимости игрока
        return true
    }

    private fun moveAway(mob: Mob, playerPosition: Position, dungeonLevel: DungeonLevel) {
        // Логика отдаления от игрока
        println("${mob.symbol} runs away from the player!")
    }
}
