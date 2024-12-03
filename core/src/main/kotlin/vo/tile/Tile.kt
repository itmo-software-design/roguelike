package vo.tile

import com.googlecode.lanterna.gui2.TextGUIGraphics

abstract class Tile(
    val walkable: Boolean,
    val visible: Boolean
) {
    abstract fun render(x: Int, y: Int, graphics: TextGUIGraphics)
}
