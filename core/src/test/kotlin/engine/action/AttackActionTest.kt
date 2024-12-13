package engine.action

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import vo.Character
import vo.DungeonLevel
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author MikhailShad
 */
class AttackActionTest {
    private lateinit var actor: Character
    private lateinit var target: Character
    private lateinit var dungeonLevel: DungeonLevel

    @BeforeTest
    fun setUp() {
        actor = mockk(relaxed = true)
        target = mockk(relaxed = true)
        dungeonLevel = mockk(relaxed = true)
    }

    @Test
    fun `attack non-reachable target`() {
        every { actor.position.euclideanDistanceTo(target.position) } returns 10

        AttackAction.perform(actor, target, dungeonLevel)

        verify(exactly = 0) { actor.attack }
        verify(exactly = 0) { target.defense }
        verify(exactly = 0) { target.health }
    }

    @Test
    fun `attack does not deal damage`() {
        every { actor.position.euclideanDistanceTo(target.position) } returns 1
        every { actor.attack } returns 1
        every { target.defense } returns 1

        AttackAction.perform(actor, target, dungeonLevel)

        verify(exactly = 1) { actor.attack }
        verify(exactly = 1) { target.defense }
        verify(exactly = 0) { target.health }
    }

    @Test
    fun `attack deals damage`() {
        every { actor.position.euclideanDistanceTo(target.position) } returns 1
        every { actor.attack } returns 2
        every { target.defense } returns 1
        every { target.isAlive } returnsMany listOf(true)

        AttackAction.perform(actor, target, dungeonLevel)

        verify(exactly = 1) { actor.attack }
        verify(exactly = 1) { target.defense }
        verify(exactly = 1) { target.health }
    }

    @Test
    fun `attack kills target`() {
        every { actor.position.euclideanDistanceTo(target.position) } returns 1
        every { actor.attack } returns 2
        every { target.defense } returns 1
        every { target.isAlive } returnsMany listOf(false)

        AttackAction.perform(actor, target, dungeonLevel)

        verify(exactly = 1) { actor.attack }
        verify(exactly = 1) { target.defense }
        verify(exactly = 1) { target.health }
        // TODO: check XP gain logic
    }
}
