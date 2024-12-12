package engine.action

import engine.factory.MobManager
import io.github.oshai.kotlinlogging.KotlinLogging
import messages.player.MoveDirection
import vo.Character
import vo.DungeonLevel
import vo.Position

/**
 * Передвижение актора
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object MoveAction : Action<Position, Boolean> {
    private val logger = KotlinLogging.logger {}

    override fun perform(actor: Character, target: Position, dungeonLevel: DungeonLevel): Boolean {
        val moveByX = if (actor.position.x <= target.x) {
            actor.position.copy(x = actor.position.x + 1)
        } else {
            actor.position.copy(x = actor.position.x - 1)
        }
        if (tryToMove(actor, moveByX, dungeonLevel)) {
            logger.debug { "$actor moved by X" }
            return true
        }

        val moveByY = if (actor.position.y < target.y) {
            actor.position.copy(y = actor.position.y + 1)
        } else {
            actor.position.copy(y = actor.position.y - 1)
        }
        if (tryToMove(actor, moveByY, dungeonLevel)) {
            logger.debug { "$actor moved by Y" }
            return true
        }


        logger.debug { "$actor failed to move to $target" }
        return false
    }

    fun perform(actor: Character, target: MoveDirection, dungeonLevel: DungeonLevel): Boolean {
        val newPosition = when (target) {
            MoveDirection.DOWN -> {
                actor.direction = MoveDirection.DOWN
                actor.position.copy(y = actor.position.y + 1)
            }

            MoveDirection.UP -> {
                actor.direction = MoveDirection.UP
                actor.position.copy(y = actor.position.y - 1)
            }

            MoveDirection.LEFT -> {
                actor.direction = MoveDirection.LEFT
                actor.position.copy(x = actor.position.x - 1)
            }

            MoveDirection.RIGHT -> {
                actor.direction = MoveDirection.RIGHT
                actor.position.copy(x = actor.position.x + 1)
            }
        }

        return tryToMove(actor, newPosition, dungeonLevel)
    }

    private fun tryToMove(
        actor: Character,
        newPosition: Position,
        dungeonLevel: DungeonLevel
    ): Boolean {
        if (!dungeonLevel.isTileFreeAt(newPosition)) {
            logger.warn {
                "Actor $actor can not move from ${actor.position} to $newPosition. " +
                        "Tile in position is ${dungeonLevel.getTileAt(newPosition)}. " +
                        "Enemy in position is ${MobManager.getMobAt(dungeonLevel, newPosition)}"
            }
            return false
        }

        logger.debug { "$actor moved from ${actor.position} to $newPosition" }
        actor.position = newPosition
        return true
    }
}
