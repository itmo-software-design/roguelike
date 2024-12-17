package vo

import com.googlecode.lanterna.TextColor
import engine.behaviour.Behaviour
import engine.behaviour.IsAliveBehaviour
import engine.factory.MobManager
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
 * Прототип моба, который может распространяться через копирование
 */
abstract class SpreadableMob(type: MobType, behaviour: Behaviour, position: Position) : Mob(
    type,
    behaviour,
    position
) {
    abstract val distance: Int
    abstract val maxDistance: Int
    abstract val root: Position
    abstract val rootType: MobType

    /**
     * Создаём копию моба
     */
    abstract fun clone(): SpreadableMob
}

class MoldMob(
    mobType: MobType,
    behaviour: Behaviour,
    position: Position,
    override val distance: Int,
    override val maxDistance: Int,
    override val root: Position,
) :
    SpreadableMob(
        mobType,
        behaviour,
        position
    ) {
    override val rootType: MobType = MobType.TOXIC_MOLD_ROOT

    constructor(
        mobType: MobType,
        behaviour: Behaviour,
        position: Position,
        maxDistance: Int,
    ) : this(mobType, behaviour, position, 0, maxDistance, position)


    override fun clone(): SpreadableMob {
        return MoldMob(
            MobType.TOXIC_MOLD,
            MobManager.getMobBehaviour(MobType.TOXIC_MOLD),
            position,
            distance + 1,
            maxDistance,
            root
        )
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
    TOXIC_MOLD_ROOT("Toxic Mold Root", 'V', TextColor.ANSI.GREEN, 3, 3, 1, 1, 3),
    TOXIC_MOLD("Toxic Mold", '+', TextColor.ANSI.GREEN_BRIGHT, 2, 3, 1, 1, 1)
}
