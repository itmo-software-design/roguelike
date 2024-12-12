package ui.console.game

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.TextColor.ANSI
import com.googlecode.lanterna.gui2.ComponentRenderer
import com.googlecode.lanterna.gui2.Panel
import com.googlecode.lanterna.gui2.TextGUIGraphics
import engine.GameSession
import engine.factory.MobManager
import ui.console.RenderContext
import vo.*

/**
 * Render game map on screen
 */
class GameMapPanelRenderer : ComponentRenderer<Panel> {
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

        val screenCenter = Position(viewWidth / 2, viewHeight / 2)

        // Рендерим все тайлы, которые были исследованы (isExplored = true)
        for (x in level.tiles.indices) {
            for (y in level.tiles[x].indices) {
                val tilePosition = Position(x, y)
                val tile = level.getTileAt(tilePosition)

                if (tile.isExplored) {
                    val screenPosition = getScreenPosition(screenCenter, tilePosition, player)

                    if (screenPosition.x in 0 until viewWidth && screenPosition.y in 0 until viewHeight) {
                        renderExploredTile(tile, screenPosition.x, screenPosition.y, graphics)
                    }
                }
            }
        }

        // BFS для рендера видимых тайлов
        val visited = mutableSetOf<Position>()
        val queue =
            ArrayDeque<Pair<Position, Int>>(player.fovRadius * player.fovRadius) // Позиция + текущее расстояние
        queue.add(player.position to 0)

        while (queue.isNotEmpty()) {
            val (current, distance) = queue.removeFirst()

            if (current in visited || distance > player.fovRadius) {
                continue
            }
            visited.add(current)

            // Определить положение на экране
            val screenPosition = getScreenPosition(screenCenter, current, player)


            // Убедиться, что не выходим за пределы экрана
            if (screenPosition.x !in 0 until viewWidth || screenPosition.y !in 0 until viewHeight) {
                continue
            }

            // Получить текущий тайл
            val tile = level.getTileAt(current)
            tile.isExplored = true
            renderTile(tile, screenPosition.x, screenPosition.y, graphics)

            // Добавить соседние тайлы, если обзор не блокируется
            if (screenPosition == screenCenter // игнорируем тайл, на котором стоим
                || !tile.type.blockSight // проверяем, что тайл не блокирует обзор
            ) {
                current.neighbours.forEach {
                    if (level.isInBounds(it)) {
                        queue.add(it to distance + 1)
                    }
                }
            }
        }

        MobManager.getActiveMobs(level).forEach {
            val screenPosition = getScreenPosition(screenCenter, it.position, player)
            renderMob(it.type, screenPosition.x, screenPosition.y, graphics)
        }

        // Покажем игрока
        renderPlayer(screenCenter.x, screenCenter.y, graphics)
    }

    override fun getPreferredSize(component: Panel?): TerminalSize {
        return TerminalSize(20, 20)
    }

    private fun getScreenPosition(
        screenCenter: Position,
        levelPosition: Position,
        player: Player
    ): Position {
        val screenX = screenCenter.x + (levelPosition.x - player.position.x)
        val screenY = screenCenter.y + (levelPosition.y - player.position.y)
        return Position(screenX, screenY)
    }

    private fun renderPlayer(x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.WHITE)
        graphics.setBackgroundColor(TextColor.ANSI.BLACK)
        graphics.putString(TerminalPosition(x, y), "$")
    }

    private fun renderMob(mobType: MobType, x: Int, y: Int, graphics: TextGUIGraphics) {
        graphics.setForegroundColor(TextColor.ANSI.WHITE)
        graphics.setBackgroundColor(TextColor.ANSI.BLACK)
        graphics.putString(TerminalPosition(x, y), mobType.symbol.toString())
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


