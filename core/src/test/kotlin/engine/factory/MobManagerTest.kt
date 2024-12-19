package engine.factory

import io.mockk.mockk
import io.mockk.verifyAll
import vo.Position
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author MikhailShad
 */
class MobManagerTest {

    private lateinit var position: Position
    private lateinit var mobFactory: MobFactory

    @BeforeTest
    fun setUp() {
        position = mockk(relaxed = true)
        mobFactory = mockk(relaxed = true)
    }

    @Test
    fun `check spawn for different mob types`() {
        repeat(4) { MobManager.spawn(mobFactory, position) }
        verifyAll {
            mobFactory.spawnWeakMob(position)
            mobFactory.spawnBasicMob(position)
            mobFactory.spawnStrongMob(position)
            mobFactory.spawnSpreadableMob(position)
        }
    }
}
