package vo

import engine.behaviour.Behaviour
import engine.behaviour.IsAliveBehaviour
import messages.player.MoveDirection

/**
 * Неигровой персонаж-моб
 *
 * @author MikhailShad
 * @since 0.0.1
 */
class Mob(
    val type: MobType,
    behaviour: Behaviour,
    position: Position
) : Character(
    type.maxHealth,
    type.baseAttack,
    type.baseDefense,
    type.fovRadius,
    position,
    MoveDirection.UP
) {
    override var symbol: Char = type.symbol

    /**
     * Поведение моба
     */
    var behaviour = IsAliveBehaviour(behaviour)

    override fun toString(): String {
        return "${type.mobName}[$health/$maxHealth] at $position"
    }
}

/**
 * Тип моба
 *
 * @author MikhailShad
 * @since 0.0.1
 */
enum class MobType(
    val mobName: String,
    val symbol: Char,
    val maxHealth: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val fovRadius: Int,
    val xp: Int
) {
    GOBLIN("Goblin", 'G', 10, 10, 1, 5, 3),
    SLIME("Slime", 'S', 5, 5, 1, 2, 2),
    BAT("Bat", 'B', 1, 3, 1, 10, 1),
    SOURCE_TOXIC_MOLD("Bat", 'V', 3, 0, 1, 10, 3),
    TOXIC_MOLD("Bat", '+', 1, 1, 1, 10, 1)
}
