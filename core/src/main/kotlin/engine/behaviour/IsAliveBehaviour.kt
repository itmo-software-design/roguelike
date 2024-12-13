package engine.behaviour

import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Декоратор для поведения живого моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
class IsAliveBehaviour(parentBehaviour: Behaviour) : BehaviourDecorator(parentBehaviour) {
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (mob.isAlive) {
            parentBehaviour.act(mob, dungeonLevel, player)
        }
    }
}
