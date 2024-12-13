package engine.action

import io.github.oshai.kotlinlogging.KotlinLogging
import vo.Character
import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Атака актора
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object AttackAction : Action<Character, Unit> {
    private val logger = KotlinLogging.logger {}

    override fun perform(actor: Character, target: Character, dungeonLevel: DungeonLevel) {
        if (actor.position.euclideanDistanceTo(target.position) > 1) {
            logger.debug { "$actor cant reach $target to attack" }
            return
        }

        val damage = actor.attack - target.defense
        logger.debug { "$actor hits $target and deals $damage damage" }
        if (damage > 0) {
            target.health -= damage
            if (!target.isAlive) {
                logger.debug { "$actor kills $target" }
                if (actor is Player) {
                    actor.addExperience((target as Mob).type.xp)
                } else {
                    // TODO: you died screen
                }
            }
        }
    }
}
