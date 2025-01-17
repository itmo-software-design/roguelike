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
    var direction: MoveDirection
) {
    val maxHealth: Int = maxHealth.coerceAtLeast(1)
    var health: Int = maxHealth
        set(value) {
            field = value.coerceIn(0, maxHealth)
        }

    private val baseAttack: Int = baseAttack.coerceAtLeast(1)
    open val attack: Int get() = baseAttack

    private val baseDefense: Int = baseDefense.coerceAtLeast(1)
    open val defense: Int get() = baseDefense

    fun isAlive(): Boolean = health > 0

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
