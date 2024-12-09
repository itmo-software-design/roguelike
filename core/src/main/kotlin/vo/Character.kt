package com.github.itmosoftwaredesign.roguelike.utils.vo

import messages.player.MoveDirection

/**
 * Абстрактный предок для персонажей игры
 */
abstract class Character(
    maxHealth: Int,
    baseAttack: Int,
    baseDefense: Int,
    var position: Position,
    var direction: MoveDirection,
    level: Int = 1
) {
    /**
     * Максимальное количество здоровья
     */
    var maxHealth: Int = maxHealth.coerceAtLeast(1)
        protected set

    /**
     * Текущее количество здоровья
     *
     * Не может опускаться ниже 0 и превышать [Character.maxHealth]
     */
    var health: Int = maxHealth
        set(value) {
            field = value.coerceIn(0, maxHealth)
        }

    /**
     * Текущий уровень персонажа
     */
    var level: Int = level
        protected set

    private val baseAttack: Int = baseAttack.coerceAtLeast(1)
    open val attack: Int get() = baseAttack

    private val baseDefense: Int = baseDefense.coerceAtLeast(1)
    open val defense: Int get() = baseDefense

    /**
     * Проверяет, жив ли персонаж
     *
     * @return `true`, если текущее здоровье больше 0; иначе `false`
     */
    fun isAlive(): Boolean = health > 0

    /**
     * Атакует другого персонажа с учетом своего значения [Character.attack]
     * и значения [Character.defense] противника
     */
    fun hit(target: Character) {
        if (!isAlive()) {
            return
        }

        val damage = attack - target.defense
        if (damage > 0) {
            target.health -= damage
        }
    }
}
