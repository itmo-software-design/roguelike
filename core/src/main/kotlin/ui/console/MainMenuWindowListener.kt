package ui.console

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.Window
import com.googlecode.lanterna.gui2.WindowBasedTextGUI
import com.googlecode.lanterna.gui2.WindowListener
import com.googlecode.lanterna.gui2.dialogs.MessageDialog
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import messages.MessageBroker
import messages.TOPIC_UI
import messages.ui.GameScreenExit
import ui.localize.localize
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
class MainMenuWindowListener(private val onClosed: () -> Unit) : WindowListener {
    override fun onInput(basePane: Window?, keyStroke: KeyStroke?, deliverEvent: AtomicBoolean?) {
        if (keyStroke == null) {
            return
        }

        when {
            keyStroke.keyType == KeyType.Escape -> {
                val result = MessageDialog.showMessageDialog(
                    basePane?.textGUI as WindowBasedTextGUI,
                    "text.exit".localize(),
                    "title.game.exit".localize(),
                    MessageDialogButton.Yes,
                    MessageDialogButton.No
                )
                if (result == MessageDialogButton.Yes) {
                    MessageBroker.send(TOPIC_UI, GameScreenExit())
                    deliverEvent?.set(true)
                    onClosed()
                }
            }
        }
    }

    override fun onUnhandledInput(
        basePane: Window?,
        keyStroke: KeyStroke?,
        hasBeenHandled: AtomicBoolean?
    ) {
        // not required
    }

    override fun onResized(window: Window?, oldSize: TerminalSize?, newSize: TerminalSize?) {
        // not required
    }

    override fun onMoved(
        window: Window?,
        oldPosition: TerminalPosition?,
        newPosition: TerminalPosition?
    ) {
        // not required
    }
}
