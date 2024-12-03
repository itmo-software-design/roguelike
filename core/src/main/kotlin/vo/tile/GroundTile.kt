package vo.tile

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.TextGUIGraphics

class GroundTile : Tile(true, true) {

    override fun render(x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.BLACK)
        graphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT)
        graphics.putString(TerminalPosition(x, y), " ")
    }

}
