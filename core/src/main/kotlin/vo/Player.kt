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

    var name: String = name
        private set

    override val attack: Int
        get() = inventory.getEquippedWeapon()?.damage ?: super.attack

    override val defense: Int
        get() = inventory.getEquippedArmor()?.defense ?: super.defense
}
