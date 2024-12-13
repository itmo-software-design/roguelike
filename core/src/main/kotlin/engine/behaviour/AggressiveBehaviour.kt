package engine.behaviour

import engine.action.AttackAction
import engine.action.CheckVisibilityAction
import engine.action.MoveAction
import engine.behaviour.AStarPathfinder.findPath
import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Агрессивное поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
class AggressiveBehaviour(parentBehaviour: Behaviour) : BehaviourDecorator(parentBehaviour) {
    /**
     * Двигается в сторону игрока, по возможности наносит урон
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (CheckVisibilityAction.perform(mob, player.position, dungeonLevel)) {
            val nextPosition = findPath(mob.position, player.position, dungeonLevel).firstOrNull()
            if (nextPosition != null) {
                if (nextPosition == player.position) {
                    AttackAction.perform(mob, player, dungeonLevel)
                } else {
                    MoveAction.perform(mob, nextPosition, dungeonLevel)
                }
            }
        } else {
            parentBehaviour.act(mob, dungeonLevel, player)
        }
    }
}
