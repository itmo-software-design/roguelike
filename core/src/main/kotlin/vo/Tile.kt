package vo

abstract class Tile(
    val walkable: Boolean,
    val visible: Boolean
) {
    abstract fun render()
}
