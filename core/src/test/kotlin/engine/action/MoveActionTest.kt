package engine.action

import engine.GameSession
import io.mockk.*
import messages.player.MoveDirection
import vo.*
import kotlin.test.*

/**
 * @author MikhailShad
 */
class MoveActionTest {
    private val actorPosition = Position(1, 1)

    private lateinit var actor: Character
    private lateinit var dungeonLevel: DungeonLevel
    private lateinit var positionCapturingSlot: CapturingSlot<Position>
    private lateinit var directionCapturingSlot: CapturingSlot<MoveDirection>

    @BeforeTest
    fun setUp() {
        mockkObject(GameSession)
        every { GameSession.player } returns mockk(relaxed = true)

        actor = mockk(relaxed = true)
        dungeonLevel = mockk(relaxed = true)
        positionCapturingSlot = slot()
        directionCapturingSlot = slot()
        every { actor.position } returns actorPosition
        every { actor.position = capture(positionCapturingSlot) } just runs
        every { actor.direction = capture(directionCapturingSlot) } just runs
    }

    @Test
    fun `move to position`() {
        val target = Position(actorPosition.x - 1, actorPosition.y)
        every { dungeonLevel.getTileAt(target) } returns Tile(TileType.FLOOR)

        assertTrue { MoveAction.perform(actor, target, dungeonLevel) }
        assertEquals(target, positionCapturingSlot.captured)
    }

//    @Test
//    fun `move right by X to position`() {
//        val target = Position(actorPosition.x + 1, actorPosition.y)
//        every { dungeonLevel.getTileAt(target) } returns Tile(TileType.FLOOR)
//
//        assertTrue { MoveAction.perform(actor, target, dungeonLevel) }
//        assertEquals(target, positionCapturingSlot.captured)
//    }
//
//    @Test
//    fun `move up by Y to position`() {
//        val target = Position(actorPosition.x, actorPosition.y + 1)
//        every { dungeonLevel.getTileAt(target) } returns Tile(TileType.FLOOR)
//
//        assertTrue { MoveAction.perform(actor, target, dungeonLevel) }
//        assertEquals(target, positionCapturingSlot.captured)
//    }
//
//    @Test
//    fun `move down by Y to position`() {
//        val target = Position(actorPosition.x, actorPosition.y - 1)
//        every { dungeonLevel.getTileAt(target) } returns Tile(TileType.FLOOR)
//
//        assertTrue { MoveAction.perform(actor, target, dungeonLevel) }
//        assertEquals(target, positionCapturingSlot.captured)
//    }

    @Test
    fun `can't move to position`() {
        val target = actorPosition
        every { dungeonLevel.getTileAt(any()) } returns Tile(TileType.WALL)

        assertFalse { MoveAction.perform(actor, target, dungeonLevel) }
        assertFalse { positionCapturingSlot.isCaptured }
    }

    @Test
    fun `move left with direction`() {
        every { dungeonLevel.getTileAt(any()) } returns Tile(TileType.FLOOR)

        assertTrue { MoveAction.perform(actor, MoveDirection.LEFT, dungeonLevel) }

        assertEquals(MoveDirection.LEFT, directionCapturingSlot.captured)
        assertEquals(actorPosition.copy(x = actor.position.x - 1), positionCapturingSlot.captured)
    }

    @Test
    fun `move right with direction`() {
        every { dungeonLevel.getTileAt(any()) } returns Tile(TileType.FLOOR)

        assertTrue { MoveAction.perform(actor, MoveDirection.RIGHT, dungeonLevel) }

        assertEquals(MoveDirection.RIGHT, directionCapturingSlot.captured)
        assertEquals(actorPosition.copy(x = actor.position.x + 1), positionCapturingSlot.captured)
    }

    @Test
    fun `move up with direction`() {
        every { dungeonLevel.getTileAt(any()) } returns Tile(TileType.FLOOR)

        assertTrue { MoveAction.perform(actor, MoveDirection.UP, dungeonLevel) }

        assertEquals(MoveDirection.UP, directionCapturingSlot.captured)
        assertEquals(actorPosition.copy(y = actor.position.y - 1), positionCapturingSlot.captured)
    }

    @Test
    fun `move down with direction`() {
        every { dungeonLevel.getTileAt(any()) } returns Tile(TileType.FLOOR)

        assertTrue { MoveAction.perform(actor, MoveDirection.DOWN, dungeonLevel) }

        assertEquals(MoveDirection.DOWN, directionCapturingSlot.captured)
        assertEquals(actorPosition.copy(y = actor.position.y + 1), positionCapturingSlot.captured)
    }

    @Test
    fun `can't move with direction but still rotates`() {
        every { dungeonLevel.getTileAt(any()) } returns Tile(TileType.WALL)

        assertFalse { MoveAction.perform(actor, MoveDirection.DOWN, dungeonLevel) }

        assertEquals(MoveDirection.DOWN, directionCapturingSlot.captured)
        assertFalse { positionCapturingSlot.isCaptured }
    }
}
