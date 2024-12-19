package engine.factory

import engine.behaviour.*
import vo.Mob
import vo.MobType
import vo.Position
import vo.SpreadableMob

/**
 * Абстрактная фабрика мобов
 *
 * @author MikhailShad
 * @since 0.0.1
 */
interface MobFactory {
    /**
     * Размещает слабого моба в выбранной позиции
     */
    fun spawnWeakMob(position: Position): Mob

    /**
     * Размещает обычного моба в выбранной позиции
     */
    fun spawnBasicMob(position: Position): Mob

    /**
     * Размещает сильного моба в выбранной позиции
     */
    fun spawnStrongMob(position: Position): Mob

    /**
     * Размещает само-реплицирующегося моба в выбранной позиции
     */
    fun spawnSpreadableMob(position: Position): SpreadableMob

    /**
     * Размещает босса
     */
    fun spawnBoss(position: Position): Mob
}

/**
 * Фабрика мобов 1 уровня
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object FirstLevelMobFactory : MobFactory {
    override fun spawnWeakMob(position: Position): Mob {
        return Mob(
            MobType.BAT,
            FearfulBehaviour(BasicBehaviour()),
            position
        )
    }

    override fun spawnBasicMob(position: Position): Mob {
        return Mob(
            MobType.SLIME,
            AggressiveBehaviour(PassiveBehaviour()),
            position
        )
    }

    override fun spawnStrongMob(position: Position): Mob {
        return Mob(
            MobType.GOBLIN,
            AggressiveBehaviour(BasicBehaviour()),
            position
        )
    }

    override fun spawnSpreadableMob(position: Position): SpreadableMob {
        return SpreadableMob(
            MobType.TOXIC_MOLD,
            IsRootAliveBehaviour(SpreadBehaviour(AttackBehaviour(PassiveBehaviour()))),
            position,
            healthPenalty = { mob -> mob.distance.coerceIn(0..mob.maxDistance) }
        )
    }

    override fun spawnBoss(position: Position): Mob = throw UnsupportedOperationException()
}

/**
 * Фабрика мобов 2 уровня
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object SecondLevelMobFactory : MobFactory {
    override fun spawnWeakMob(position: Position): Mob {
        val mobType = MobType.BAT
        return Mob(
            mobType,
            AggressiveBehaviour(BasicBehaviour()),
            position,
            maxHealth = mobType.maxHealth + 1,
            baseAttack = mobType.baseAttack + 1,
            baseDefense = mobType.baseDefense + 1,
            xp = mobType.xp + 1
        )
    }

    override fun spawnBasicMob(position: Position): Mob {
        val mobType = MobType.SLIME
        return Mob(
            mobType,
            AggressiveBehaviour(PassiveBehaviour()),
            position,
            maxHealth = mobType.maxHealth + 2,
            baseAttack = mobType.baseAttack + 2,
            baseDefense = mobType.baseDefense + 2,
            xp = mobType.xp + 2
        )
    }

    override fun spawnStrongMob(position: Position): Mob {
        val mobType = MobType.GOBLIN
        return Mob(
            mobType,
            AggressiveBehaviour(PassiveBehaviour()),
            position,
            maxHealth = mobType.maxHealth + 3,
            baseAttack = mobType.baseAttack + 3,
            baseDefense = mobType.baseDefense + 3,
            xp = mobType.xp + 3
        )
    }

    override fun spawnSpreadableMob(position: Position): SpreadableMob {
        return SpreadableMob(
            MobType.TOXIC_MOLD,
            SpreadBehaviour(AttackBehaviour(BasicBehaviour())),
            position,
            maxDistance = Int.MAX_VALUE,
            healthPenalty = { 0 }
        )
    }

    override fun spawnBoss(position: Position): Mob = throw UnsupportedOperationException()
}

/**
 * Фабрика босса
 *
 * @author MikhailShad
 * @since 0.0.1
 */
object BossFactory : MobFactory {
    override fun spawnWeakMob(position: Position): Mob = throw UnsupportedOperationException()

    override fun spawnBasicMob(position: Position): Mob = throw UnsupportedOperationException()

    override fun spawnStrongMob(position: Position): Mob = throw UnsupportedOperationException()

    override fun spawnSpreadableMob(position: Position): SpreadableMob =
        throw UnsupportedOperationException()

    override fun spawnBoss(position: Position): Mob = Mob(
        MobType.DUNGEON_MASTER, AggressiveBehaviour(BasicBehaviour()),
        position
    )

}
