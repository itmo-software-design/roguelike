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
class IsAliveBehaviour<T : Behaviour>(private val behaviour: T) : Behaviour {
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (mob.isAlive) {
            behaviour.act(mob, dungeonLevel, player)
        }
    }
}
