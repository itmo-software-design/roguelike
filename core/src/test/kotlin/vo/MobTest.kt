package vo

import engine.behaviour.Behaviour
import engine.behaviour.FearfulBehaviour
import engine.state.ConfusedState
import engine.state.NormalState
import engine.state.PanicState
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author MikhailShad
 */
class MobTest {
    companion object {
        const val MAX_HEALTH = 10
        const val CRITICAL_HEALTH = 5
    }

    private lateinit var mob: Mob
    private lateinit var behaviour: Behaviour
    private lateinit var position: Position

    @BeforeTest
    fun setUp() {
        behaviour = mockk()
        position = mockk()
        mob = Mob(
            MobType.GOBLIN,
            behaviour,
            position,
            maxHealth = MAX_HEALTH,
            criticalHealth = CRITICAL_HEALTH
        )
    }

    @Test
    fun `check normal state`() {
        assertTrue { mob.state is NormalState }
        assertEquals(behaviour, mob.state.getBehaviour())
    }

    @Test
    fun `check panic state`() {
        mob.health = CRITICAL_HEALTH - 1
        assertTrue { mob.state is PanicState }
        assertTrue { mob.state.getBehaviour() is FearfulBehaviour }
    }

    @Test
    fun `check expirable state back to normal state`() {
        assertTrue { mob.state is NormalState }

        val duration = 5
        mob.applyTemporaryEffect(duration = duration)
        repeat(duration) {
            assertTrue { mob.state is ConfusedState }
        }

        assertTrue { mob.state is NormalState }
    }

    @Test
    fun `check expirable state back to panic state`() {
        mob.health = CRITICAL_HEALTH - 1
        assertTrue { mob.state is PanicState }

        val duration = 5
        mob.applyTemporaryEffect(duration = duration)
        repeat(duration) {
            assertTrue { mob.state is ConfusedState }
        }

        assertTrue { mob.state is PanicState }
    }
}
