package com.github.itmosoftwaredesign.roguelike.utils.vo

class Inventory : Iterable<Item> {
    private val items: MutableList<Item> = mutableListOf()
    private var equippedWeapon: Weapon? = null
    private var equippedArmor: Armor? = null

    fun addItem(item: Item) {
        items.add(item)
        println("${item.name} added to the inventory.")
    }

    fun removeItem(item: Item) {
        if (items.remove(item)) {
            if (item == equippedWeapon) {
                equippedWeapon = null
                println("${item.name} unequipped.")
            }
            if (item == equippedArmor) {
                equippedArmor = null
                println("${item.name} unequipped.")
            }
            println("${item.name} removed from the inventory.")
        }
    }

    fun equipItem(item: Item) {
        when (item) {
            is Weapon -> {
                equippedWeapon = item
                println("${item.name} equipped as a weapon.")
            }

            is Armor -> {
                equippedArmor = item
                println("${item.name} equipped as armor.")
            }

            else -> println("${item.name} cannot be equipped.")
        }
    }

    fun unequipItem(item: Item) {
        when (item) {
            is Weapon -> if (equippedWeapon == item) {
                equippedWeapon = null
                println("${item.name} unequipped as a weapon.")
            }

            is Armor -> if (equippedArmor == item) {
                equippedArmor = null
                println("${item.name} unequipped as armor.")
            }
        }
    }

    fun getEquippedWeapon(): Weapon? = equippedWeapon
    fun getEquippedArmor(): Armor? = equippedArmor

    override fun iterator(): Iterator<Item> = items.iterator()
}
