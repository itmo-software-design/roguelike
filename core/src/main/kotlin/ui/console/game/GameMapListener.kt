package ui.console.game

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
import messages.TOPIC_PLAYER
import messages.TOPIC_UI
import messages.player.MoveDirection
import messages.player.MovePlayer
import messages.player.OpenInventory
import messages.player.PlayerInteract
import messages.ui.GameScreenExit
import ui.localize.localize
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
class GameMapListener : WindowListener {
    lateinit var onClosed: () -> Unit

    override fun onInput(basePane: Window?, keyStroke: KeyStroke?, deliverEvent: AtomicBoolean?) {
        if (keyStroke == null) {
            return
        }

        when {
            keyStroke.keyType == KeyType.ArrowUp || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
                'w',
                'W'
            ) -> {
                val event = MovePlayer(MoveDirection.UP)
                MessageBroker.send(TOPIC_PLAYER, event)
                deliverEvent?.set(true)
            }

            keyStroke.keyType == KeyType.ArrowRight || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
                'd',
                'D'
            ) -> {
                val event = MovePlayer(MoveDirection.RIGHT)
                MessageBroker.send(TOPIC_PLAYER, event)
                deliverEvent?.set(true)
            }

            keyStroke.keyType == KeyType.ArrowDown || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
                's',
                'S'
            ) -> {
                val event = MovePlayer(MoveDirection.DOWN)
                MessageBroker.send(TOPIC_PLAYER, event)
                deliverEvent?.set(true)
            }

            keyStroke.keyType == KeyType.ArrowLeft || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
                'a',
                'A'
            ) -> {
                val event = MovePlayer(MoveDirection.LEFT)
                MessageBroker.send(TOPIC_PLAYER, event)
                deliverEvent?.set(true)
            }

            keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
                'i',
                'I'
            ) -> {
                MessageBroker.send(TOPIC_PLAYER, OpenInventory())
                deliverEvent?.set(true)
            }

            keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
                'e',
                'E'
            ) -> {
                MessageBroker.send(TOPIC_PLAYER, PlayerInteract())
                deliverEvent?.set(true)
            }

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
        // not used
    }

    override fun onResized(window: Window?, oldSize: TerminalSize?, newSize: TerminalSize?) {
        // not used
    }

    override fun onMoved(
        window: Window?,
        oldPosition: TerminalPosition?,
        newPosition: TerminalPosition?
    ) {
        // not used
    }
}
