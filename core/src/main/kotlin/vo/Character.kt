package vo

import messages.player.MoveDirection

/**
 * Абстрактный предок для персонажей игры
 *
 * @author MikhailShad
 * @since 0.0.1
 */
abstract class Character(
    maxHealth: Int,
    baseAttack: Int,
    baseDefense: Int,
    fovRadius: Int = 1,
    position: Position,
    var direction: MoveDirection
) : Entity(position) {
    /**
     * Максимальное количество здоровья
     */
    open val maxHealth: Int = maxHealth.coerceAtLeast(1)

    /**
     * Текущее количество здоровья
     *
     * Не может опускаться ниже 0 и превышать [Character.maxHealth]
     */
    open var health: Int = maxHealth
        set(value) {
            field = value.coerceIn(0, maxHealth)
        }

    private val baseAttack: Int = baseAttack.coerceAtLeast(1)
    open val attack: Int get() = baseAttack

    private val baseDefense: Int = baseDefense.coerceAtLeast(1)
    open val defense: Int get() = baseDefense

    /**
     * Радиус обзора
     */
    open var fovRadius = fovRadius
        protected set

    /**
     * Проверяет, жив ли персонаж
     *
     * @return `true`, если текущее здоровье больше 0; иначе `false`
     */
    val isAlive: Boolean get() = health > 0

    override fun toString(): String {
        return "${javaClass}[$health/$maxHealth] at $position"
    }
}
