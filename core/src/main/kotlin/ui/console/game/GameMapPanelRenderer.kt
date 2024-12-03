package ui.console.game

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.ComponentRenderer
import com.googlecode.lanterna.gui2.Panel
import com.googlecode.lanterna.gui2.TextGUIGraphics
import ui.console.RenderContext
import vo.tile.Tile

class GameMapPanelRenderer(private val map: Array<Array<Tile>>) : ComponentRenderer<Panel> {

    override fun drawComponent(graphics: TextGUIGraphics?, component: Panel?) {
        if (graphics == null) {
            return
        }
        graphics.backgroundColor = RenderContext.backgroundColor
        graphics.fill(' ')
        for (y in map.indices) {
            for (x in map[y].indices) {
                val tile = map[y][x]
                tile.render(x, y, graphics)
            }
        }
    }

    override fun getPreferredSize(component: Panel?): TerminalSize {
        return TerminalSize(map[0].size, map.size)
    }
}


