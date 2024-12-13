package engine.behaviour

import engine.action.AttackAction
import engine.action.CheckVisibilityAction
import engine.action.MoveAction
import io.mockk.*
import vo.DungeonLevel
import vo.Mob
import vo.Player
import vo.Position
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author MikhailShad
 */
class AggressiveBehaviourTest {
    private lateinit var behaviour: AggressiveBehaviour
    private lateinit var parentBehaviour: Behaviour
    private lateinit var mob: Mob
    private lateinit var dungeonLevel: DungeonLevel
    private lateinit var player: Player

    @BeforeTest
    fun setUp() {
        mockkObject(CheckVisibilityAction)
        mockkObject(AttackAction)
        mockkObject(MoveAction)

        mob = mockk(relaxed = true)
        player = mockk(relaxed = true)
        dungeonLevel = mockk(relaxed = true)
        parentBehaviour = mockk<Behaviour>(relaxed = true)
        behaviour = spyk(AggressiveBehaviour(parentBehaviour))

        every { mob.position } returns Position.ZERO
    }

    @Test
    fun `attack player when they are near`() {
        every { player.position } returns Position(1, 0)
        every { CheckVisibilityAction.perform(any(), any(), any()) } returns true

        behaviour.act(mob, dungeonLevel, player)

        verify { AttackAction.perform(mob, player, dungeonLevel) }
        verify { MoveAction wasNot Called }
        verify { parentBehaviour wasNot Called }
    }

    @Test
    fun `move towards player when they are visible`() {
        every { player.position } returns Position(10, 10)
        every { CheckVisibilityAction.perform(any(), any(), any()) } returns true

        behaviour.act(mob, dungeonLevel, player)

        verify { AttackAction wasNot Called }
        verify { MoveAction.perform(mob, any<Position>(), dungeonLevel) }
        verify { parentBehaviour wasNot Called }
    }

    @Test
    fun `behave basically when player is not visible`() {
        every { CheckVisibilityAction.perform(any(), any(), any()) } returns false

        behaviour.act(mob, dungeonLevel, player)

        verify { AttackAction wasNot Called }
        verify { MoveAction wasNot Called }
        verify(exactly = 1) { parentBehaviour.act(mob, dungeonLevel, player) }
        confirmVerified(parentBehaviour)
    }
}
