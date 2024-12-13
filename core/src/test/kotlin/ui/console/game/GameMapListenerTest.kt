package ui.console.game

import com.googlecode.lanterna.gui2.Window
import com.googlecode.lanterna.gui2.WindowBasedTextGUI
import com.googlecode.lanterna.gui2.dialogs.MessageDialog
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import io.mockk.*
import messages.MessageBroker
import messages.TOPIC_PLAYER
import messages.player.MoveDirection
import messages.player.MovePlayer
import messages.player.OpenInventory
import messages.player.PlayerInteract
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ui.localize.localize
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Stream
import kotlin.test.assertTrue

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
class GameMapListenerTest {
    private lateinit var listener: GameMapListener

    @BeforeEach
    fun setUp() {
        listener = GameMapListener()
    }

    @Test
    fun `test pass nulls arguments`() {
        mockkObject(MessageBroker)

        listener.onInput(null, null, null)

        verify(exactly = 0) { MessageBroker.send(any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("movement cases")
    fun `test movement key pressed`(keyStroke: KeyStroke, moveDirection: MoveDirection) {
        val deliveredEvent = AtomicBoolean()

        mockkObject(MessageBroker)

        listener.onInput(null, keyStroke, deliveredEvent)

        verify { MessageBroker.send(TOPIC_PLAYER, MovePlayer(moveDirection)) }

        assertTrue { deliveredEvent.get() }
    }

    @ParameterizedTest
    @MethodSource("open inventory cases")
    fun `test open inventory`(keyStroke: KeyStroke) {
        val deliveredEvent = AtomicBoolean()

        mockkObject(MessageBroker)

        listener.onInput(null, keyStroke, deliveredEvent)

        verify { MessageBroker.send(TOPIC_PLAYER, any(OpenInventory::class)) }

        assertTrue { deliveredEvent.get() }
    }

    @ParameterizedTest
    @MethodSource("interaction cases")
    fun `test interact`(keyStroke: KeyStroke) {
        val deliveredEvent = AtomicBoolean()

        mockkObject(MessageBroker)

        listener.onInput(null, keyStroke, deliveredEvent)

        verify { MessageBroker.send(TOPIC_PLAYER, any(PlayerInteract::class)) }

        assertTrue { deliveredEvent.get() }
    }

    @Test
    fun `test reject to close screen`() {
        val deliveredEvent = AtomicBoolean()

        mockkStatic(MessageDialog::class)

        val window = mockk<Window>()

        val textGUI = mockk<WindowBasedTextGUI>()
        every { window.textGUI } returns textGUI

        every {
            MessageDialog.showMessageDialog(
                textGUI,
                "text.exit".localize(),
                "title.game.exit".localize(),
                MessageDialogButton.Yes,
                MessageDialogButton.No
            )
        } returns MessageDialogButton.No

        listener.onInput(window, KeyStroke(KeyType.Escape), deliveredEvent)

        assertTrue { deliveredEvent.get() }
    }

    @Test
    fun `test accept close screen`() {
        val deliveredEvent = AtomicBoolean()

        mockkStatic(MessageDialog::class)

        val window = mockk<Window>()

        val textGUI = mockk<WindowBasedTextGUI>()
        every { window.textGUI } returns textGUI

        var closed = false
        listener.onClosed = {
            closed = true
        }

        every {
            MessageDialog.showMessageDialog(
                textGUI,
                "text.exit".localize(),
                "title.game.exit".localize(),
                MessageDialogButton.Yes,
                MessageDialogButton.No
            )
        } returns MessageDialogButton.Yes

        listener.onInput(window, KeyStroke(KeyType.Escape), deliveredEvent)

        assertTrue { deliveredEvent.get() }
        assertTrue { closed }
    }

    companion object {
        @JvmStatic
        fun `movement cases`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    KeyStroke('w', false, false), MoveDirection.UP,
                ),
                Arguments.of(
                    KeyStroke('W', false, false), MoveDirection.UP,
                ),
                Arguments.of(
                    KeyStroke(KeyType.ArrowUp), MoveDirection.UP,
                ),

                Arguments.of(
                    KeyStroke('s', false, false), MoveDirection.DOWN,
                ),
                Arguments.of(
                    KeyStroke('S', false, false), MoveDirection.DOWN,
                ),
                Arguments.of(
                    KeyStroke(KeyType.ArrowDown), MoveDirection.DOWN,
                ),

                Arguments.of(
                    KeyStroke('a', false, false), MoveDirection.LEFT,
                ),
                Arguments.of(
                    KeyStroke('A', false, false), MoveDirection.LEFT,
                ),
                Arguments.of(
                    KeyStroke(KeyType.ArrowLeft), MoveDirection.LEFT,
                ),

                Arguments.of(
                    KeyStroke('d', false, false), MoveDirection.RIGHT,
                ),
                Arguments.of(
                    KeyStroke('D', false, false), MoveDirection.RIGHT,
                ),
                Arguments.of(
                    KeyStroke(KeyType.ArrowRight), MoveDirection.RIGHT,
                ),
            )
        }

        @JvmStatic
        fun `open inventory cases`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    KeyStroke('i', false, false)
                ),
                Arguments.of(
                    KeyStroke('I', false, false)
                ),
            )
        }

        @JvmStatic
        fun `interaction cases`(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    KeyStroke('e', false, false)
                ),
                Arguments.of(
                    KeyStroke('E', false, false)
                ),
            )
        }
    }

}
