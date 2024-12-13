package engine.behaviour

import io.mockk.*
import org.junit.jupiter.api.assertDoesNotThrow
import vo.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author MikhailShad
 */
class BasicBehaviourTest {
    private lateinit var behaviour: BasicBehaviour
    private lateinit var mob: Mob
    private lateinit var dungeonLevel: DungeonLevel
    private lateinit var player: Player

    @BeforeTest
    fun setUp() {
        behaviour = spyk(BasicBehaviour())
        mob = mockk(relaxed = true)
        player = mockk(relaxed = true)
        dungeonLevel = mockk(relaxed = true)

        every { dungeonLevel.rooms } returns listOf(
            Room(Position(1, 1), 3, 3),
            Room(Position(50, 50), 3, 3),
            Room(Position(10, 10), 3, 3),
        )
    }

    @AfterTest
    fun tearDown() {
        verify(exactly = 0) { player wasNot Called }
        confirmVerified(player)
    }

    @Test
    fun `start behaviour`() {
        assertDoesNotThrow { behaviour.act(mob, dungeonLevel, player) }
    }
}
