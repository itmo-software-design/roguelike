package vo

import engine.behaviour.Behavior
import messages.player.MoveDirection

/**
 * Неигровой персонаж-моб
 *
 * @author MikhailShad
 * @since 0.0.1
 */
class Mob(
    val type: MobType,
    val behavior: Behavior,
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
    val fovRadius: Int
) {
    GOBLIN("Goblin", 'G', 10, 1, 1, 5),
    SLIME("Slime", 'S', 5, 1, 1, 2),
    BAT("Bat", 'B', 1, 1, 1, 10)
}
