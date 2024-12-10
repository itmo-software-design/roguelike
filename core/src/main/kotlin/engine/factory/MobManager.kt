package engine.factory

import engine.behaviour.BasicBehaviour
import vo.DungeonLevel
import vo.Mob
import vo.MobType
import vo.Position
import kotlin.random.Random

/**
 * Фабрика мобов
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object MobManager {

    /**
     * Размещает моба в выбранной позиции
     */
    fun spawn(position: Position): Mob {
        val mobType = when (Random.nextInt(100)) {
            in 0 until 10 -> MobType.GOBLIN
            in 10 until 50 -> MobType.SLIME
            else -> MobType.BAT
        }

        return Mob(mobType, BasicBehaviour(), position)
    }

    fun getActiveMobs(dungeonLevel: DungeonLevel): List<Mob> {
        return dungeonLevel.enemies.filter { it.isAlive() }
    }

    fun getMobAt(dungeonLevel: DungeonLevel, position: Position): Mob? {
        return dungeonLevel.enemies.find { it.position == position && it.isAlive() }
    }
}
