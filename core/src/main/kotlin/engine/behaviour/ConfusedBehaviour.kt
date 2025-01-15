package engine.behaviour

import engine.action.MoveAction
import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Сконфуженное поведение
 *
 * @author MikhailShad
 */
class ConfusedBehaviour : Behaviour {
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        mob.position.neighbours.shuffled().forEach {
            if (MoveAction.perform(mob, it, dungeonLevel)) {
                return
            }
        }
    }
}
