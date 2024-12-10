package engine.behaviour

import vo.DungeonLevel
import vo.Mob
import vo.Player
import vo.Position

/**
 * Агрессивное поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
class AggressiveBehavior : Behavior {
    /**
     * Двигается в сторону игрока, по возможности наносит урон
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (isPlayerVisible(mob.position, player.position, dungeonLevel)) {
            moveTowards(mob, player.position, dungeonLevel)
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

    private fun moveTowards(mob: Mob, targetPosition: Position, dungeonLevel: DungeonLevel) {
        // Логика передвижения к игроку
        println("${mob} moves towards the player!")
    }
}
