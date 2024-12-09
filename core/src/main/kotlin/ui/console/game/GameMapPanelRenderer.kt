package ui.console.game

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.TextColor.ANSI
import com.googlecode.lanterna.gui2.ComponentRenderer
import com.googlecode.lanterna.gui2.Panel
import com.googlecode.lanterna.gui2.TextGUIGraphics
import engine.GameSession
import ui.console.RenderContext
import vo.DungeonLevel
import vo.Position
import vo.Tile
import vo.TileType

/**
 * Render game map on screen
 */
class GameMapPanelRenderer : ComponentRenderer<Panel> {
    private val fovLimit = 10
    override fun drawComponent(graphics: TextGUIGraphics?, component: Panel?) {
        if (graphics == null || component == null) {
            return
        }

        graphics.backgroundColor = RenderContext.backgroundColor
        graphics.fill(' ')

        val level = GameSession.currentDungeonLevel
        val player = GameSession.player

        val terminalSize = graphics.size
        val viewWidth = terminalSize.columns
        val viewHeight = terminalSize.rows

        val centerX = viewWidth / 2
        val centerY = viewHeight / 2

        // Рендерим все тайлы, которые были исследованы (isExplored = true)
        for (x in level.tiles.indices) {
            for (y in level.tiles[x].indices) {
                val tile = level.tiles[x][y]
                if (tile.isExplored) {
                    val screenX = centerX + (x - player.position.x)
                    val screenY = centerY + (y - player.position.y)

                    if (screenX in 0 until viewWidth && screenY in 0 until viewHeight) {
                        renderExploredTile(tile, screenX, screenY, graphics)
                    }
                }
            }
        }

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
            tile.isExplored = true
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
        dungeonLevel: DungeonLevel,
        x: Int,
        y: Int,
        distance: Int,
        queue: ArrayDeque<Pair<Position, Int>>
    ) {
        val tileToCheckVisibility = dungeonLevel.tiles.getOrNull(x)?.getOrNull(y)
        if (tileToCheckVisibility != null && !tileToCheckVisibility.type.blockSight) {
            queue.add(Position(x, y) to distance + 1)
        }
    }

    private fun renderPlayer(x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.WHITE)
        graphics.setBackgroundColor(TextColor.ANSI.BLACK)
        graphics.putString(TerminalPosition(x, y), "$")
    }

    private fun renderExploredTile(tile: Tile, x: Int, y: Int, graphics: TextGUIGraphics) {
        val (_, _, char) = getTileRenderParams(tile)
        graphics.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT)
        graphics.setBackgroundColor(TextColor.ANSI.BLACK)
        graphics.putString(TerminalPosition(x, y), char)
    }

    private fun renderTile(tile: Tile, x: Int, y: Int, graphics: TextGUIGraphics) {
        val (fgColor, bgColor, char) = getTileRenderParams(tile)
        graphics.setForegroundColor(fgColor)
        graphics.setBackgroundColor(bgColor)
        graphics.putString(TerminalPosition(x, y), char)
    }

    private fun getTileRenderParams(tile: Tile): Triple<ANSI, ANSI, String> {
        return when (tile.type) {
            TileType.FLOOR -> Triple(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK_BRIGHT, ".")
            TileType.WALL -> Triple(TextColor.ANSI.MAGENTA, TextColor.ANSI.DEFAULT, "#")
            TileType.WATER -> Triple(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.BLUE, "~")
            TileType.GRASS -> Triple(TextColor.ANSI.GREEN, TextColor.ANSI.GREEN_BRIGHT, "|")
            TileType.HALL -> Triple(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK_BRIGHT, "o")
            TileType.DOOR -> Triple(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK, "+")
            TileType.CONSUMABLE -> Triple(
                TextColor.ANSI.GREEN_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT,
                "c"
            )

            TileType.WEAPON -> Triple(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK_BRIGHT, "w")
            TileType.ARMOR -> Triple(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK_BRIGHT, "a")
            TileType.MOB -> Triple(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.RED, "X")
            TileType.PORTAL -> Triple(TextColor.ANSI.WHITE, TextColor.ANSI.WHITE_BRIGHT, "0")
            else -> Triple(TextColor.ANSI.BLACK, TextColor.ANSI.BLACK, " ")
        }
    }
}

private val terminalSizeComparator = Comparator.comparing(TerminalSize::getRows, Int::compareTo)
    .thenComparing(TerminalSize::getColumns, Int::compareTo)

private operator fun TerminalSize.compareTo(preferredSize: TerminalSize): Int {
    return terminalSizeComparator.compare(this, preferredSize)
}


