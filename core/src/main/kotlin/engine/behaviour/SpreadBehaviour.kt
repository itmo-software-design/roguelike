package engine.behaviour

import engine.factory.MobManager
import io.github.oshai.kotlinlogging.KotlinLogging
import vo.DungeonLevel
import vo.Mob
import vo.Player
import vo.SpreadableMob
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

        if (randomizer.nextDouble(1.0) >= mob.spreadProbability) {
            logger.debug { "$mob skipped spawning" }
            return parentBehaviour.act(mob, dungeonLevel, player)
        }

        val spreadPosition = mob.position.neighbours.firstOrNull {
            MobManager.canSpawnAt(dungeonLevel, it)
        }
        if (spreadPosition == null) {
            logger.debug { "$mob can not spawn child mob at ${mob.position.neighbours}" }
            return parentBehaviour.act(mob, dungeonLevel, player)
        }

        val spread = mob.clone()
        if (spread == null) {
            logger.debug { "$mob can not spawn child mob due to incorrect inner conditions" }
            return parentBehaviour.act(mob, dungeonLevel, player)
        }

        spread.position = spreadPosition
        logger.debug { "Spreading $mob to position $spreadPosition" }
        dungeonLevel.enemies.add(spread)

        return parentBehaviour.act(mob, dungeonLevel, player) // все еще выполняем базовое поведение
    }

}
