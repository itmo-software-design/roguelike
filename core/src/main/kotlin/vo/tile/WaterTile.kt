package vo.tile

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.TextGUIGraphics

class WaterTile : Tile(false, true) {

    override fun render(x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT)
        graphics.setBackgroundColor(TextColor.ANSI.BLUE)
        graphics.putString(TerminalPosition(x, y), "~")
    }

}
