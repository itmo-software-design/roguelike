package ui.console.game

import com.github.itmosoftwaredesign.roguelike.utils.vo.Player
import com.googlecode.lanterna.gui2.Label
import com.googlecode.lanterna.gui2.Window
import com.googlecode.lanterna.gui2.Window.Hint
import engine.GameSession
import io.mockk.*
import messages.MessageBroker
import messages.TOPIC_UI
import messages.ui.GameScreenOpened
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.console.UIContext
import kotlin.test.assertEquals

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
class GameMapScreenTest {

    private lateinit var mockWindow: Window
    private lateinit var mockUIContext: UIContext
    private lateinit var onReturnCallback: () -> Unit
    private lateinit var gameMapScreen: GameMapScreen

    @BeforeEach
    fun setUp() {
        mockWindow = mockk(relaxed = true)
        mockUIContext = mockk(relaxed = true) {
            every { playerName } returns "TestPlayer"
        }
        onReturnCallback = mockk(relaxed = true)

        every { mockWindow.hints } returns setOf(Hint.EXPANDED)

        mockkObject(MessageBroker)

        gameMapScreen = GameMapScreen(mockWindow, mockUIContext, onReturnCallback)
    }

    @Test
    fun `initialization sets up components correctly`() {
        val components = gameMapScreen.children
        assertEquals("The Tail of TestPlayer.", (components.first() as Label).text)

        assertEquals(6, components.size)

        verify { mockWindow.setHints(setOf(Hint.EXPANDED, Hint.FULL_SCREEN, Hint.NO_DECORATIONS)) }
    }

    @Test
    fun `onBeforeDrawing updates player label when player is initialized`() {
        val mockPlayer = mockk<Player> {
            every { name } returns "Hero"
            every { health } returns 50
            every { maxHealth } returns 100
        }
        mockkObject(GameSession)
        every { GameSession.isPlayerInitialized() } returns true
        every { GameSession.player } returns mockPlayer

        gameMapScreen.onBeforeDrawing()

        val playerLabel = gameMapScreen.children.first() as Label
        assertEquals(
            "The Tail of Hero. 50/100 HP",
            playerLabel.text
        )
    }

    @Test
    fun `gameMapListener onClosed restores hints and triggers callback`() {
        val listenerSlot = slot<GameMapListener>()
        verify { mockWindow.addWindowListener(capture(listenerSlot)) }

        listenerSlot.captured.onClosed()

        verify { mockWindow.removeWindowListener(listenerSlot.captured) }
        verify { mockWindow.setHints(setOf(Hint.EXPANDED)) }
        verify { onReturnCallback.invoke() }
    }

    @Test
    fun `GameScreenOpened message is sent on initialization`() {
        verify { MessageBroker.send(TOPIC_UI, GameScreenOpened("TestPlayer")) }
    }
}
