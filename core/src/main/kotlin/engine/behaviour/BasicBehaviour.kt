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


        val nextDestination = path.removeFirstOrNull()
        if (nextDestination == null) {
            parentBehaviour.act(mob, dungeonLevel, player) // если вот вообще некуда идти, то я хз, что делать
        } else {
            if (!MoveAction.perform(mob, nextDestination, dungeonLevel)) {
                // Если подвигаться не получилось, попробуем перестроить маршрут
                rebuildPath(mob, dungeonLevel)
            }
            mob.health += 1 // TODO: we need to find a good place where we can regenerate mobs
        }
    }

    private fun rebuildPath(mob: Mob, dungeonLevel: DungeonLevel) {
        path.clear()

        dungeonLevel.rooms.sortedBy {
            it.center.euclideanDistanceTo(mob.position) // пытаемся построить маршрут от самой дальней к ближайшей комнате
        }.forEach {
            val foundPath = findPath(mob.position, it.center, dungeonLevel)
            if (foundPath.isNotEmpty()) {
                path.addAll(foundPath)
                return@forEach
            }
        }
    }
}
