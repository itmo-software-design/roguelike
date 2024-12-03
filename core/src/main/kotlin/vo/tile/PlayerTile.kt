package vo.tile

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.TextGUIGraphics

class PlayerTile(val stayOnTile: Tile) : Tile(false, true) {

    override fun render(x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.WHITE)
        graphics.setBackgroundColor(TextColor.ANSI.BLACK)
        graphics.putString(TerminalPosition(x, y), "$")
    }

}
