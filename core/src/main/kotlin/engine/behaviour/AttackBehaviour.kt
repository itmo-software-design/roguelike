package engine.behaviour

import engine.action.AttackAction
import engine.action.CheckVisibilityAction
import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Атакуем рядом стоящего игрока
 *
 * @since sibmaks
 * @since 0.0.1
 */
class AttackBehaviour(parentBehaviour: Behaviour) : BehaviourDecorator(parentBehaviour) {
    /**
     * Двигается в сторону игрока, по возможности наносит урон
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (CheckVisibilityAction.perform(mob, player.position, dungeonLevel)) {
            AttackAction.perform(mob, player, dungeonLevel)
        } else {
            parentBehaviour.act(mob, dungeonLevel, player)
        }
    }
}
