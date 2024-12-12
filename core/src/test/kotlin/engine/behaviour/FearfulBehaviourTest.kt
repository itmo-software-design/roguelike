package engine.behaviour


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
class FearfulBehaviourTest {
    private lateinit var behaviour: FearfulBehaviour
    private lateinit var parentBehaviour: Behaviour
    private lateinit var mob: Mob
    private lateinit var dungeonLevel: DungeonLevel
    private lateinit var player: Player


    @BeforeTest
    fun setUp() {
        mockkObject(CheckVisibilityAction)
        mockkObject(MoveAction)

        mob = mockk(relaxed = true)
        player = mockk(relaxed = true)
        dungeonLevel = mockk(relaxed = true)
        parentBehaviour = mockk<Behaviour>(relaxed = true)
        behaviour = spyk(FearfulBehaviour(parentBehaviour))
    }

    @Test
    fun `behave basically if player is not visible`() {
        every { CheckVisibilityAction.perform(mob, player, dungeonLevel) } returns false

        behaviour.act(mob, dungeonLevel, player)

        verify(exactly = 1) { parentBehaviour.act(mob, dungeonLevel, player) }
        confirmVerified(parentBehaviour)
    }

    @Test
    fun `try to run away if player is visible`() {
        every { CheckVisibilityAction.perform(mob, player, dungeonLevel) } returns true
        val neighbourMoPosition = mockk<Position>(relaxed = true)
        every { neighbourMoPosition.manhattanDistanceTo(any()) } returnsMany listOf(1, 2, 3)
        every { mob.position.neighbours } returns listOf(
            neighbourMoPosition,
            neighbourMoPosition,
            neighbourMoPosition
        )
        every { dungeonLevel.isInBounds(any()) } returns true

        behaviour.act(mob, dungeonLevel, player)

        verify(atLeast = 1) { MoveAction.perform(mob, any<Position>(), dungeonLevel) }
        confirmVerified(MoveAction)
        verify { parentBehaviour wasNot called }
    }

}
