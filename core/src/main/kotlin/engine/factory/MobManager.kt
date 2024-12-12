package engine.factory

import engine.behaviour.*
import vo.DungeonLevel
import vo.Mob
import vo.MobType
import vo.Position

/**
 * Фабрика мобов
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object MobManager {
    private var mobCount = 0

    /**
     * Размещает моба в выбранной позиции
     */
    fun spawn(position: Position): Mob {
        val mobType = decideMobType()
        val behaviour = when (mobType) {
            MobType.GOBLIN -> IsAliveBehaviour(AggressiveBehaviour(BasicBehaviour()))
            MobType.SLIME -> IsAliveBehaviour(AggressiveBehaviour(PassiveBehaviour()))
            MobType.BAT -> IsAliveBehaviour(FearfulBehaviour(BasicBehaviour()))
        }

        return Mob(mobType, behaviour, position)
    }

    private fun decideMobType(): MobType {
//        when (Random.nextInt(100)) {
//            in 0 until 10 -> MobType.GOBLIN
//            in 10 until 50 -> MobType.SLIME
//            else -> MobType.BAT
//        }

        // В целях тестирования по очереди создадим моба каждого типа
        return when (mobCount++ % 3) {
            0 -> MobType.GOBLIN
            1 -> MobType.SLIME
            else -> MobType.BAT
        }
    }

    fun getActiveMobs(dungeonLevel: DungeonLevel): List<Mob> {
        return dungeonLevel.enemies.filter { it.isAlive }
    }

    fun getMobAt(dungeonLevel: DungeonLevel, position: Position): Mob? {
        return dungeonLevel.enemies.find { it.position == position && it.isAlive }
    }
}
