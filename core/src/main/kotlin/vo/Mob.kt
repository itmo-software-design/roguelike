package vo

import com.googlecode.lanterna.TextColor
import engine.behaviour.Behaviour
import engine.behaviour.IsAliveBehaviour
import messages.player.MoveDirection

/**
 * Неигровой персонаж-моб
 *
 * @author MikhailShad
 * @since 0.0.1
 */
open class Mob(
    val type: MobType,
    behaviour: Behaviour,
    position: Position,
    maxHealth: Int? = null,
    baseAttack: Int? = null,
    baseDefense: Int? = null,
    fovRadius: Int? = null,
    xp: Int? = null
) : Character(
    maxHealth ?: type.maxHealth,
    baseAttack ?: type.baseAttack,
    baseDefense ?: type.baseDefense,
    fovRadius ?: type.fovRadius,
    position,
    MoveDirection.UP
) {
    /**
     * Имя моба
     */
    val name = type.mobName

    /**
     * Количество опыта за убийство моба
     */
    val xp = xp ?: type.xp

    /**
     * Символ для отображения моба на карте
     */
    override var symbol = type.symbol

    /**
     * Поведение моба
     */
    var behaviour = IsAliveBehaviour(behaviour)

    override fun toString(): String {
        return "${name}[$health/$maxHealth] at $position"
    }
}

/**
 * Прототип моба, который может распространяться через копирование
 */
class SpreadableMob(
    type: MobType,
    behaviour: Behaviour,
    position: Position,
    val healthPenalty: (SpreadableMob) -> Int,
    val spreadProbability: Double = 0.3,
    val root: SpreadableMob? = null,
    val maxDistance: Int = 3
) : Mob(
    type,
    behaviour,
    position
) {
    val distance: Int get() = root?.position?.manhattanDistanceTo(this.position) ?: 0

    /**
     * Создаём копию моба
     */
    fun clone(): SpreadableMob? {
        val spreadHealth = this.health - this.healthPenalty(this)
        if (spreadHealth <= 0) {
            return null // нельзя создать потомка, который сразу мертвый
        }

        if (distance >= maxDistance) {
            return null // нельзя создать потомка дальше максимальной дистанции разрастания
        }

        val clone = SpreadableMob(
            this.type,
            this.behaviour,
            this.position,
            this.healthPenalty,
            this.spreadProbability,
            root ?: this,
            this.distance + 1
        )
        clone.health = spreadHealth
        return clone
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
    val color: TextColor,
    val maxHealth: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val fovRadius: Int,
    val xp: Int
) {
    GOBLIN("Goblin", 'G', TextColor.ANSI.RED, 10, 10, 1, 5, 3),
    SLIME("Slime", 'S', TextColor.ANSI.YELLOW, 5, 5, 1, 2, 2),
    BAT("Bat", 'B', TextColor.ANSI.WHITE, 1, 3, 1, 10, 1),
    TOXIC_MOLD("Toxic Mold", 'V', TextColor.ANSI.GREEN, 3, 3, 1, 1, 3),
}
