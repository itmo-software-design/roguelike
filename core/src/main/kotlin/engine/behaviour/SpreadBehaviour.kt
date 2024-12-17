package engine.behaviour

import engine.factory.MobManager
import io.github.oshai.kotlinlogging.KotlinLogging
import vo.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Распространяемся на рядом стоящие ячейки
 *
 * @since sibmaks
 * @since 0.0.1
 */
class SpreadBehaviour(parentBehaviour: Behaviour) : BehaviourDecorator(parentBehaviour) {
    private val logger = KotlinLogging.logger {}

    private val randomizer = Random(System.currentTimeMillis())

    /**
     * Распространяемся на рядом стоящие ячейки
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (mob !is SpreadableMob) {
            logger.warn { "$mob is not a SpreadableMob" }
            return parentBehaviour.act(mob, dungeonLevel, player)
        }

        if (randomizer.nextDouble(1.0) >= 0.7) {
            return parentBehaviour.act(mob, dungeonLevel, player)
        }

        val x1 = max(mob.position.x - 1, 0)
        val x2 = min(mob.position.x + 2, dungeonLevel.width)
        val y1 = max(mob.position.y - 1, 0)
        val y2 = min(mob.position.y + 2, dungeonLevel.height)
        val mobPosition = generateRandomPosition(x1, x2, y1, y2)

        if (!MobManager.canSpawnAt(dungeonLevel, mobPosition)) {
            return parentBehaviour.act(mob, dungeonLevel, player)
        }
        val spread = mob.clone()
        val health = mob.health - (spread.maxDistance - spread.distance)
        if (spread.maxDistance < spread.distance || health < 0) {
            return parentBehaviour.act(mob, dungeonLevel, player)
        }
        spread.health = health
        spread.position = mobPosition

        logger.debug { "Spreading $mob to position $mobPosition" }
        dungeonLevel.enemies.add(spread)
    }

    private fun generateRandomPosition(x1: Int, x2: Int, y1: Int, y2: Int): Position {
        val x = randomizer.nextInt(x1, x2)
        val y = randomizer.nextInt(y1, y2)
        return Position(x, y)
    }
}
