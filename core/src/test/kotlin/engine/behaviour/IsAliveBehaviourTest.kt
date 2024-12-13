package engine.behaviour

import io.mockk.*
import vo.DungeonLevel
import vo.Mob
import vo.Player
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author MikhailShad
 */
class IsAliveBehaviourTest {
    private lateinit var behaviour: IsAliveBehaviour
    private lateinit var parentBehaviour: Behaviour
    private lateinit var mob: Mob
    private lateinit var dungeonLevel: DungeonLevel
    private lateinit var player: Player


    @BeforeTest
    fun setUp() {
        mob = mockk(relaxed = true)
        player = mockk(relaxed = true)
        dungeonLevel = mockk(relaxed = true)
        parentBehaviour = mockk<Behaviour>(relaxed = true)
        behaviour = spyk(IsAliveBehaviour(parentBehaviour))
    }

    @Test
    fun `behave if alive`() {
        every { mob.isAlive } returns true

        behaviour.act(mob, dungeonLevel, player)

        verify(exactly = 1) { parentBehaviour.act(mob, dungeonLevel, player) }
        confirmVerified(parentBehaviour)
        verify(exactly = 1) { mob.isAlive }
        confirmVerified(mob)
        verify { player wasNot Called }
        verify { dungeonLevel wasNot Called }
    }

    @Test
    fun `do nothing if dead`() {
        every { mob.isAlive } returns false

        behaviour.act(mob, dungeonLevel, player)

        verify { parentBehaviour wasNot Called }
        verify(exactly = 1) { mob.isAlive }
        confirmVerified(mob)
        verify { player wasNot Called }
        verify { dungeonLevel wasNot Called }
    }
}
