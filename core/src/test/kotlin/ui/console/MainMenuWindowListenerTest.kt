package ui.console

import com.googlecode.lanterna.gui2.Window
import com.googlecode.lanterna.gui2.WindowBasedTextGUI
import com.googlecode.lanterna.gui2.dialogs.MessageDialog
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.localize.localize
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertTrue

/**
 *
 * @author sibmaks
 * @since
 */
class MainMenuWindowListenerTest {
    private lateinit var listener: MainMenuWindowListener

    @BeforeEach
    fun setUp() {
        listener = MainMenuWindowListener {}
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
}
