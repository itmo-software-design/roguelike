package vo.tile

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.TextGUIGraphics

class WallTile : Tile(false, true) {

    override fun render(x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.MAGENTA)
        graphics.setBackgroundColor(TextColor.ANSI.DEFAULT)
        graphics.putString(TerminalPosition(x, y), "#")
    }

}
