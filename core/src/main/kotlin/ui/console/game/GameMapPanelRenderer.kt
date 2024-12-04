package ui.console.game

import com.github.itmosoftwaredesign.roguelike.utils.vo.Level
import com.github.itmosoftwaredesign.roguelike.utils.vo.Position
import com.github.itmosoftwaredesign.roguelike.utils.vo.Tile
import com.github.itmosoftwaredesign.roguelike.utils.vo.TileType
import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.ComponentRenderer
import com.googlecode.lanterna.gui2.Panel
import com.googlecode.lanterna.gui2.TextGUIGraphics
import engine.GameSession
import ui.console.RenderContext

class GameMapPanelRenderer : ComponentRenderer<Panel> {
    private val fovLimit = 10
    override fun drawComponent(graphics: TextGUIGraphics?, component: Panel?) {
        if (graphics == null || component == null) {
            return
        }

        graphics.backgroundColor = RenderContext.backgroundColor
        graphics.fill(' ')

        val level = GameSession.currentLevel
        val player = GameSession.player

        val terminalSize = graphics.size
        val viewWidth = terminalSize.columns
        val viewHeight = terminalSize.rows

        val centerX = viewWidth / 2
        val centerY = viewHeight / 2

        // BFS для рендера видимых тайлов
        val visited = mutableSetOf<Position>()
        val queue =
            ArrayDeque<Pair<Position, Int>>(fovLimit * fovLimit) // Позиция + текущее расстояние
        queue.add(player.position to 0)

        while (queue.isNotEmpty()) {
            val (current, distance) = queue.removeFirst()

            if (current in visited || distance > fovLimit) {
                continue
            }
            visited.add(current)

            // Определить положение на экране
            val screenX = centerX + (current.x - player.position.x)
            val screenY = centerY + (current.y - player.position.y)

            // Убедиться, что не выходим за пределы экрана
            if (screenX !in 0 until viewWidth || screenY !in 0 until viewHeight) continue

            // Получить текущий тайл
            val tile = level.tiles.getOrNull(current.x)?.getOrNull(current.y) ?: continue

            // Отрисовать тайл
            renderTile(tile, screenX, screenY, graphics)

            // Добавить соседние тайлы, если обзор не блокируется
//            addTileToQueueIfVisible(level, current.x - 1, current.y, distance, queue)
//            addTileToQueueIfVisible(level, current.x + 1, current.y, distance, queue)
//            addTileToQueueIfVisible(level, current.x, current.y - 1, distance, queue)
//            addTileToQueueIfVisible(level, current.x, current.y + 1, distance, queue)
            if ((screenX == centerX && screenY == centerY) // игнорируем тайл, на котором стоим
                || !tile.type.blockSight // проверяем, что тайл не блокирует обзор
            ) {
                queue.add(Position(current.x - 1, current.y) to distance + 1)
                queue.add(Position(current.x + 1, current.y) to distance + 1)
                queue.add(Position(current.x, current.y - 1) to distance + 1)
                queue.add(Position(current.x, current.y + 1) to distance + 1)
            }
        }

        // Покажем игрока
        renderPlayer(centerX, centerY, graphics)
    }

    override fun getPreferredSize(component: Panel?): TerminalSize {
        return TerminalSize(20, 20)
    }

    private fun addTileToQueueIfVisible(
        level: Level,
        x: Int,
        y: Int,
        distance: Int,
        queue: ArrayDeque<Pair<Position, Int>>
    ) {
        val tileToCheckVisibility = level.tiles.getOrNull(x)?.getOrNull(y)
        if (tileToCheckVisibility != null && !tileToCheckVisibility.type.blockSight) {
            queue.add(Position(x, y) to distance + 1)
        }
    }

    private fun renderPlayer(x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.WHITE)
        graphics.setBackgroundColor(TextColor.ANSI.BLACK)
        graphics.putString(TerminalPosition(x, y), "$")
    }

    private fun renderTile(tile: Tile, x: Int, y: Int, graphics: TextGUIGraphics) {
        when (tile.type) {
            TileType.FLOOR -> {
                graphics.setForegroundColor(TextColor.ANSI.BLACK)
                graphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT)
                graphics.putString(TerminalPosition(x, y), " ")
            }

            TileType.WALL -> {
                graphics.setForegroundColor(TextColor.ANSI.MAGENTA)
                graphics.setBackgroundColor(TextColor.ANSI.DEFAULT)
                graphics.putString(TerminalPosition(x, y), "#")
            }

            TileType.WATER -> {
                graphics.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT)
                graphics.setBackgroundColor(TextColor.ANSI.BLUE)
                graphics.putString(TerminalPosition(x, y), "~")
            }

            TileType.GRASS -> {
                graphics.setForegroundColor(TextColor.ANSI.GREEN)
                graphics.setBackgroundColor(TextColor.ANSI.GREEN_BRIGHT)
                graphics.putString(TerminalPosition(x, y), "|")
            }

            TileType.HALL -> {
                graphics.setForegroundColor(TextColor.ANSI.YELLOW)
                graphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT)
                graphics.putString(TerminalPosition(x, y), "o")
            }

            TileType.DOOR -> {
                graphics.setForegroundColor(TextColor.ANSI.YELLOW)
                graphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT)
                graphics.putString(TerminalPosition(x, y), "+")
            }

            else -> {
                // Ничего не делаем, так как фон мы в самом начале задали
            }
        }
    }
}

private val terminalSizeComparator = Comparator.comparing(TerminalSize::getRows, Int::compareTo)
    .thenComparing(TerminalSize::getColumns, Int::compareTo)

private operator fun TerminalSize.compareTo(preferredSize: TerminalSize): Int {
    return terminalSizeComparator.compare(this, preferredSize)
}


