package engine.behaviour

import engine.factory.MobManager
import io.github.oshai.kotlinlogging.KotlinLogging
import vo.DungeonLevel
import vo.Mob
import vo.Player
import vo.SpreadableMob

/**
 * Декоратор для поведения живого моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
class IsRootAliveBehaviour(parentBehaviour: Behaviour) : BehaviourDecorator(parentBehaviour) {
    private val logger = KotlinLogging.logger {}

    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (mob !is SpreadableMob) {
            logger.warn { "$mob is not a SpreadableMob" }
            return parentBehaviour.act(mob, dungeonLevel, player)
        }

        val root = MobManager.getMobAt(dungeonLevel, mob.root)
        if (root == null || root.health <= 0 || root.type != mob.rootType) {
            mob.health--
        } else {
            return parentBehaviour.act(mob, dungeonLevel, player)
        }
    }
}
