package engine.factory

import vo.Randomizer
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
        val behaviour = when (mobType) {
            MobType.GOBLIN -> IsAliveBehaviour(AggressiveBehaviour(BasicBehaviour()))
            MobType.SLIME -> IsAliveBehaviour(AggressiveBehaviour(PassiveBehaviour()))
            MobType.BAT -> IsAliveBehaviour(FearfulBehaviour(BasicBehaviour()))
        }

        return Mob(mobType, behaviour, position)
    }

    /**
     * Сгенерировать мобов и добавить их на уровень
     */
    fun generateMobs(dungeonLevel: DungeonLevel) {
        val nMobs = Randomizer.nextInt(dungeonLevel.rooms.size, 2 * dungeonLevel.rooms.size)

        var mobCreated = 0
        while (mobCreated < nMobs) { // рандомно размещает мобов по комнатам
            val room = dungeonLevel.rooms[Randomizer.nextInt(dungeonLevel.rooms.size)]

            val mobPosition = Randomizer.generateRandomPosition(
                room.bottomLeft.x + 1, room.topRight.x,
                room.bottomLeft.y + 1, room.topRight.y
            )

            if (canSpawnAt(dungeonLevel, mobPosition)) {
                val mob = spawn(mobPosition)
                dungeonLevel.enemies.add(mob)
                mobCreated += 1
            }
        }
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
