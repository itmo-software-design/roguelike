package engine.behaviour

import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Пассивное поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
class PassiveBehaviour : BasicBehaviour() {
    /**
     * Ничего не делает
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        return
    }
}
