package engine.factory

import engine.Randomizer
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verifyAll
import vo.Position
import kotlin.random.Random
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
        mockkObject(Randomizer)
        every { Randomizer.random } returns Random(42)
        position = mockk(relaxed = true)
        mobFactory = mockk(relaxed = true)
    }

    @Test
    fun `check spawn for different mob types`() {
        repeat(10) { MobManager.spawn(mobFactory, position) }
        verifyAll {
            mobFactory.spawnWeakMob(position)
            mobFactory.spawnBasicMob(position)
            mobFactory.spawnStrongMob(position)
            mobFactory.spawnSpreadableMob(position)
        }
    }
}
