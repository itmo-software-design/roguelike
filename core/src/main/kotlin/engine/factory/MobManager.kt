package engine.factory

import engine.behaviour.*
import vo.*

/**
 * Фабрика мобов
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object MobManager {
    private var mobCount = 0

    /**
     * Проверяет, можно ли расположить моба в указанной точке на уровне
     */
    fun canSpawnAt(dungeonLevel: DungeonLevel, position: Position): Boolean {
        val tile = dungeonLevel.getTileAt(position)

        return !tile.type.blocked // тайл не блокирует движение
            && getMobAt(dungeonLevel, position) == null // в этой позиции нет мобов
            && dungeonLevel.startPosition != position // и нас тут нет тоже
    }

    /**
     * Размещает моба в выбранной позиции
     */
    fun spawn(position: Position): Mob {
        val mobType = decideMobType()

        val behaviour = getMobBehaviour(mobType)

        return when (mobType) {
            MobType.GOBLIN -> Mob(mobType, behaviour, position)
            MobType.SLIME -> Mob(mobType, behaviour, position)
            MobType.BAT -> Mob(mobType, behaviour, position)
            MobType.TOXIC_MOLD_ROOT -> MoldMob(mobType, behaviour, position, 2)
            MobType.TOXIC_MOLD -> throw IllegalArgumentException("Impossible to spawn TOXIC_MOLD")
        }
    }

    fun getMobBehaviour(mobType: MobType): IsAliveBehaviour {
        return when (mobType) {
            MobType.GOBLIN -> IsAliveBehaviour(AggressiveBehaviour(BasicBehaviour()))
            MobType.SLIME -> IsAliveBehaviour(AggressiveBehaviour(PassiveBehaviour()))
            MobType.BAT -> IsAliveBehaviour(FearfulBehaviour(BasicBehaviour()))
            MobType.TOXIC_MOLD_ROOT -> IsAliveBehaviour(
                AttackBehaviour(
                    SpreadBehaviour(
                        PassiveBehaviour()
                    )
                )
            )

            MobType.TOXIC_MOLD -> IsAliveBehaviour(
                IsRootAliveBehaviour(
                    AttackBehaviour(
                        SpreadBehaviour(
                            PassiveBehaviour()
                        )
                    )
                )
            )
        }
    }

    private fun decideMobType(): MobType {
//        when (Random.nextInt(100)) {
//            in 0 until 10 -> MobType.GOBLIN
//            in 10 until 50 -> MobType.SLIME
//            else -> MobType.BAT
//        }

        // В целях тестирования по очереди создадим моба каждого типа
        return when (mobCount++ % MobType.entries.size) {
            0 -> MobType.GOBLIN
            1 -> MobType.SLIME
            2 -> MobType.BAT
            else -> MobType.TOXIC_MOLD_ROOT
        }
    }

    fun getActiveMobs(dungeonLevel: DungeonLevel): List<Mob> {
        return dungeonLevel.enemies.filter { it.isAlive }
    }

    fun getMobAt(dungeonLevel: DungeonLevel, position: Position): Mob? {
        return dungeonLevel.enemies.find { it.position == position && it.isAlive }
    }
}
