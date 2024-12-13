package com.github.itmosoftwaredesign.roguelike.utils.vo

class Inventory : Iterable<_root_ide_package_.vo.Item> {
    private val items: MutableList<_root_ide_package_.vo.Item> = mutableListOf()
    private var equippedWeapon: _root_ide_package_.vo.Weapon? = null
    private var equippedArmor: _root_ide_package_.vo.Armor? = null

    fun addItem(item: _root_ide_package_.vo.Item) {
        items.add(item)
        println("${item.name} added to the inventory.")
    }

    fun removeItem(item: _root_ide_package_.vo.Item) {
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

    fun equipItem(item: _root_ide_package_.vo.Item) {
        when (item) {
            is _root_ide_package_.vo.Weapon -> {
                equippedWeapon = item
                println("${item.name} equipped as a weapon.")
            }

            is _root_ide_package_.vo.Armor -> {
                equippedArmor = item
                println("${item.name} equipped as armor.")
            }

            else -> println("${item.name} cannot be equipped.")
        }
    }

    fun unequipItem(item: _root_ide_package_.vo.Item) {
        when (item) {
            is _root_ide_package_.vo.Weapon -> if (equippedWeapon == item) {
                equippedWeapon = null
                println("${item.name} unequipped as a weapon.")
            }

            is _root_ide_package_.vo.Armor -> if (equippedArmor == item) {
                equippedArmor = null
                println("${item.name} unequipped as armor.")
            }
        }
    }

    fun getEquippedWeapon(): _root_ide_package_.vo.Weapon? = equippedWeapon
    fun getEquippedArmor(): _root_ide_package_.vo.Armor? = equippedArmor

    override fun iterator(): Iterator<_root_ide_package_.vo.Item> = items.iterator()
}
