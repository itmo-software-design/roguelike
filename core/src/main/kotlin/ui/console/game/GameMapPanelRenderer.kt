package ui.console.game

import com.github.itmosoftwaredesign.roguelike.utils.vo.Position
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.ComponentRenderer
import com.googlecode.lanterna.gui2.Panel
import com.googlecode.lanterna.gui2.TextGUIGraphics
import ui.console.RenderContext
import vo.tile.PlayerTile
import vo.tile.Tile
import kotlin.math.max
import kotlin.math.min

class GameMapPanelRenderer(private val map: Array<Array<Tile>>) : ComponentRenderer<Panel> {

    override fun drawComponent(graphics: TextGUIGraphics?, component: Panel?) {
        if (graphics == null || component == null) {
            return
        }
        graphics.backgroundColor = RenderContext.backgroundColor
        graphics.fill(' ')

        val size = component.size
        val preferredSize = getPreferredSize(component)

        if (size < preferredSize) {
            var playerPosition = Position(0, 0)
            for (y in map.indices) {
                for (x in map[y].indices) {
                    val tile = map[y][x]
                    if (tile is PlayerTile) {
                        playerPosition = Position(x, y)
                        break
                    }
                }
            }
            var top = max(0, playerPosition.y - size.rows / 2)

            if (top + size.rows > map.size) {
                top -= (size.rows + top) - map.size
            }
            val bottom = min(map.size, top + size.rows)

            var left = max(0, playerPosition.x - size.columns / 2)
            if (left + size.columns > map[0].size) {
                left -= (size.columns + left) - map[0].size
            }
            val right = min(map[0].size, left + size.columns)
            for (y in top until bottom) {
                for (x in left until right) {
                    val tile = map[y][x]
                    tile.render(x - left, y - top, graphics)
                }
            }
        } else {
            for (y in map.indices) {
                for (x in map[y].indices) {
                    val tile = map[y][x]
                    tile.render(x, y, graphics)
                }
            }
        }
    }

    override fun getPreferredSize(component: Panel?): TerminalSize {
        return TerminalSize(map[0].size, map.size)
    }
}

private val terminalSizeComparator = Comparator.comparing(TerminalSize::getRows, Int::compareTo)
    .thenComparing(TerminalSize::getColumns, Int::compareTo)

private operator fun TerminalSize.compareTo(preferredSize: TerminalSize): Int {
    return terminalSizeComparator.compare(this, preferredSize)
}


