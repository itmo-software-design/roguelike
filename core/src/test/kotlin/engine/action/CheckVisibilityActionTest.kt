package engine.action

import io.mockk.every
import io.mockk.mockk
import vo.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author MikhailShad
 */
class CheckVisibilityActionTest {
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
    fun `target is outside of FOV`() {
        every { actor.position.manhattanDistanceTo(target.position) } returns 1
        every { actor.fovRadius } returns 0

        assertFalse { CheckVisibilityAction.perform(actor, target, dungeonLevel) }
    }

    @Test
    fun `target is not visible behind an obstacle`() {
        every { actor.position } returns Position.ZERO
        every { target.position } returns Position(2, 1)
        every { actor.fovRadius } returns 10

        val visibleTile = mockk<Tile>()
        every { visibleTile.type } returns TileType.FLOOR
        val blockVisibleTile = mockk<Tile>()
        every { blockVisibleTile.type } returns TileType.WALL
        every { dungeonLevel.getTileAt(any()) } returnsMany listOf(
            visibleTile,
            blockVisibleTile,
            visibleTile
        )

        assertFalse { CheckVisibilityAction.perform(actor, target, dungeonLevel) }
    }

    @Test
    fun `target is visible `() {
        every { actor.position } returns Position.ZERO
        every { target.position } returns Position(2, 1)
        every { actor.fovRadius } returns 10

        val visibleTile = mockk<Tile>()
        every { visibleTile.type } returns TileType.FLOOR
        every { dungeonLevel.getTileAt(any()) } returnsMany listOf(
            visibleTile,
            visibleTile,
            visibleTile
        )

        assertTrue { CheckVisibilityAction.perform(actor, target, dungeonLevel) }
    }
}
