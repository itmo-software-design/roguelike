package engine.action

import io.github.oshai.kotlinlogging.KotlinLogging
import vo.Character
import vo.DungeonLevel
import vo.Position
import kotlin.math.abs

/**
 * Актор всматривается в темноту, пытаясь увидеть свою цель
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object CheckVisibilityAction : Action<Position, Boolean> {
    private val logger = KotlinLogging.logger {}

    override fun perform(actor: Character, target: Position, dungeonLevel: DungeonLevel): Boolean {
        val distance = actor.position.manhattanDistanceTo(target)
        if (distance > actor.fovRadius) {
            logger.trace { "$actor does not see $target" }
            return false
        }

        val lineOfSight = getLineOfSight(actor.position, target)
        val tilesAtLineOfSight = lineOfSight.map { dungeonLevel.getTileAt(it) }
        return tilesAtLineOfSight.drop(1) // игнорируем стартовый тайл, так как мы уже на нем стоим
            .dropLast(1) // и последний, куда упирается взгляд
            .all { tile -> !tile.type.blockSight }
    }

    /**
     * Алгоритм Брезенхэма для построения прямой линии между двумя точками в дискретной двумерной сетке
     */
    private fun getLineOfSight(start: Position, end: Position): List<Position> {
        val line = mutableListOf<Position>()

        var x1 = start.x
        var y1 = start.y
        val x2 = end.x
        val y2 = end.y

        val dx = abs(x2 - x1)
        val dy = abs(y2 - y1)
        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1

        var err = dx - dy

        while (true) {
            line.add(Position(x1, y1))

            if (x1 == x2 && y1 == y2) break

            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x1 += sx
            }
            if (e2 < dx) {
                err += dx
                y1 += sy
            }
        }

        return line
    }
}
