package engine.behaviour

import engine.action.MoveAction
import engine.behaviour.AStarPathfinder.findPath
import vo.DungeonLevel
import vo.Mob
import vo.Player
import vo.Position

/**
 * Базовое поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
open class BasicBehaviour(parentBehaviour: Behaviour = PassiveBehaviour()) :
    BehaviourDecorator(parentBehaviour) {
    private val path: ArrayDeque<Position> = ArrayDeque()

    /**
     * Бродит по уровню
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (path.isEmpty()) {
            rebuildPath(mob, dungeonLevel)
        }

        val nextDestination = path.removeFirst()
        if (!MoveAction.perform(mob, nextDestination, dungeonLevel)) {
            // Если подвигаться не получилось, попробуем перестроить маршрут
            rebuildPath(mob, dungeonLevel)
        }
    }

    private fun rebuildPath(mob: Mob, dungeonLevel: DungeonLevel) {
        path.clear()
        val sortedRooms = dungeonLevel.rooms.maxBy {
            it.center.euclideanDistanceTo(mob.position)
        }
        path.addAll(findPath(mob.position, sortedRooms.center, dungeonLevel))
    }
}
