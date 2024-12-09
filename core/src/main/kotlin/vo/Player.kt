package com.github.itmosoftwaredesign.roguelike.utils.vo

import messages.player.MoveDirection

/**
 * Класс игрока
 */
class Player(
    name: String,
    maxHealth: Int,
    baseAttack: Int,
    baseDefense: Int,
    position: Position,
    direction: MoveDirection = MoveDirection.UP,
    var inventory: Inventory = Inventory()
) : Character(maxHealth, baseAttack, baseDefense, position, direction) {


    /**
     * Текущее количество опыта до повышения уровня
     */
    var xp: Int = 0
        private set

    /**
     * Имя игрока
     */
    var name: String = name
        private set

    override val attack: Int
        get() = inventory.getEquippedWeapon()?.damage ?: super.attack

    override val defense: Int
        get() = inventory.getEquippedArmor()?.defense ?: super.defense

    fun levelUp() {
        level += 1
        xp = 0 // TODO: надо бы все-таки отслеживать, сколько опыта остается после повышения уровня
        maxHealth += 5
    }
}
