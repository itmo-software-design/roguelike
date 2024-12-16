package engine.behaviour

import engine.action.AttackAction
import engine.action.CheckVisibilityAction
import engine.action.MoveAction
import engine.behaviour.AStarPathfinder.findPath
import engine.factory.MobManager
import vo.DungeonLevel
import vo.Mob
import vo.Player

/**
 * Распространяемся на рядом стоящие ячейки
 *
 * @since sibmaks
 * @since 0.0.1
 */
class SpreadBehaviour(parentBehaviour: Behaviour) : BehaviourDecorator(parentBehaviour) {
    /**
     * Распространяемся на рядом стоящие ячейки
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        val position = mob.position.copy(mob.position.x + 1, mob.position.y)
        if (MobManager.canSpawnAt(dungeonLevel, mob.position)) {
            val nextPosition = findPath(mob.position, player.position, dungeonLevel).firstOrNull()
            if (nextPosition == player.position) {
                AttackAction.perform(mob, player, dungeonLevel)
            }
        } else {
            parentBehaviour.act(mob, dungeonLevel, player)
        }
    }
}
