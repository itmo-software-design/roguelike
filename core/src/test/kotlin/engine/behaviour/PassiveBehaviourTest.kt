package engine.behaviour


import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import vo.DungeonLevel
import vo.Mob
import vo.Player
import kotlin.test.Test

/**
 * @author MikhailShad
 */
class PassiveBehaviourTest {
    @Test
    fun `check passive behaviour is really passive`() {
        val behaviour = PassiveBehaviour()
        val mob = mockk<Mob>()
        val player = mockk<Player>()
        val dungeonLevel = mockk<DungeonLevel>()

        behaviour.act(mob, dungeonLevel, player)

        verify { mob wasNot Called }
        confirmVerified(mob)

        verify { player wasNot Called }
        confirmVerified(player)

        verify { dungeonLevel wasNot Called }
        confirmVerified(dungeonLevel)
    }
}
