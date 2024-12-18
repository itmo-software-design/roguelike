package engine.factory

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
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
        MobManager.spawn(mobFactory, position)
        verify(exactly = 1) { mobFactory.spawnWeakMob(position) }
        MobManager.spawn(mobFactory, position)
        verify(exactly = 1) { mobFactory.spawnBasicMob(position) }
        MobManager.spawn(mobFactory, position)
        verify(exactly = 1) { mobFactory.spawnStrongMob(position) }
        MobManager.spawn(mobFactory, position)
        verify(exactly = 1) { mobFactory.spawnSpreadableMob(position) }
        confirmVerified(mobFactory)
    }
}
