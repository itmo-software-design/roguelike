package engine.factory

import vo.DungeonLevel
import vo.Mob
import vo.MobType
import vo.Position
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Фабрика мобов
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object MobManager {
    private var mobCount = 0
    private val random = Random(42)

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
    fun spawn(mobFactory: MobFactory, position: Position): Mob {
        // В целях тестирования по очереди создадим моба каждого типа
        return when (mobCount++ % MobType.entries.size) {
            0 -> mobFactory.spawnWeakMob(position)
            1 -> mobFactory.spawnBasicMob(position)
            2 -> mobFactory.spawnStrongMob(position)
            else -> mobFactory.spawnSpreadableMob(position)
        }
    }

    /**
     * Сгенерировать мобов и добавить их на уровень
     */
    fun generateMobs(mobFactory: MobFactory, dungeonLevel: DungeonLevel) {
        val nMobs = random.nextInt(dungeonLevel.rooms.size, 2 * dungeonLevel.rooms.size)

        var mobCreated = 0
        while (mobCreated < nMobs) { // рандомно размещает мобов по комнатам
            val room = dungeonLevel.rooms[random.nextInt(dungeonLevel.rooms.size)]

            val mobPosition = Position(
                random.nextInt(room.bottomLeft.x + 1, room.topRight.x),
                random.nextInt(room.bottomLeft.y + 1, room.topRight.y)
            )

            if (canSpawnAt(dungeonLevel, mobPosition)) {
                val mob = spawn(mobFactory, mobPosition)
                dungeonLevel.enemies.add(mob)
                mobCreated += 1
            }
        }
    }

    fun getActiveMobs(dungeonLevel: DungeonLevel): List<Mob> {
        return dungeonLevel.enemies.filter { it.isAlive }
    }

    fun getMobAt(dungeonLevel: DungeonLevel, position: Position): Mob? {
        return dungeonLevel.enemies.find { it.position == position && it.isAlive }
    }
}


