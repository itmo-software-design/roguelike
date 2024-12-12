package engine.behaviour

import engine.action.CheckVisibilityAction
import engine.action.MoveAction
import vo.DungeonLevel
import vo.Mob
import vo.Player


/**
 * Пугливое поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
class FearfulBehaviour(parentBehaviour: Behaviour) : BehaviourDecorator(parentBehaviour) {
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (CheckVisibilityAction.perform(mob, player, dungeonLevel)) {
            val positionsToRunAway = mob.position.neighbours.filter { dungeonLevel.isInBounds(it) }
                .sortedByDescending { it.manhattanDistanceTo(player.position) }
            for (position in positionsToRunAway) {
                if (MoveAction.perform(mob, position, dungeonLevel)) {
                    return
                }
            }
        } else {
            parentBehaviour.act(mob, dungeonLevel, player)
        }
    }
}
