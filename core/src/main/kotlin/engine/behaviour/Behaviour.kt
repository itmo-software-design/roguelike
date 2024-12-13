package engine.behaviour

import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
interface Behaviour {
    /**
     * Выполняет действие согласно своей модели поведения
     *
     * @param mob моб-актор, выполняющий действие
     * @param dungeonLevel уровень, на котором находится моб
     * @param player игрок на уровне
     */
    fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player)
}
