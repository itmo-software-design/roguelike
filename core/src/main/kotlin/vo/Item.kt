package vo


open class Item(
    val name: String,
    val description: String
) {
    open fun use() {
        println("$name used.")
    }
}

interface Equippable {
    fun equip()
}

class Weapon(
    name: String,
    description: String,
    val damage: Int
) : Item(name, description), Equippable {

    override fun equip() {
        println("$name equipped. Damage: $damage")
    }
}

class Armor(
    name: String,
    description: String,
    val defense: Int
) : Item(name, description), Equippable {

    override fun equip() {
        println("$name equipped. Defense: $defense")
    }
}

class Consumable(
    name: String,
    description: String,
    val effect: String
) : Item(name, description) {

    fun consume() {
        println("$name consumed. Effect: $effect")
    }

    override fun use() {
        consume()
    }
}
