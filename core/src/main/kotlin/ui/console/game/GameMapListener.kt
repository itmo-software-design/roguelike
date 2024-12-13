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
 * Game window event listener. Map keyboard events into game's events.
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
            isMoveUp(keyStroke) -> {
                sendMove(deliverEvent, MoveDirection.UP)
            }

            isMoveRight(keyStroke) -> {
                sendMove(deliverEvent, MoveDirection.RIGHT)
            }

            isMoveDown(keyStroke) -> {
                sendMove(deliverEvent, MoveDirection.DOWN)
            }

            isMoveLeft(keyStroke) -> {
                sendMove(deliverEvent, MoveDirection.LEFT)
            }

            isOpenInventory(keyStroke) -> {
                MessageBroker.send(TOPIC_PLAYER, OpenInventory())
                deliverEvent?.set(true)
            }

            isInteract(keyStroke) -> {
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
                deliverEvent?.set(true)
                if (result == MessageDialogButton.Yes) {
                    MessageBroker.send(TOPIC_UI, GameScreenExit())
                    onClosed()
                }
            }
        }
    }

    private fun sendMove(deliverEvent: AtomicBoolean?, direction: MoveDirection) {
        val event = MovePlayer(direction)
        MessageBroker.send(TOPIC_PLAYER, event)
        deliverEvent?.set(true)
    }

    private fun isInteract(keyStroke: KeyStroke) =
        keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
            'e', 'E'
        )

    private fun isOpenInventory(keyStroke: KeyStroke) =
        keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
            'i', 'I'
        )

    private fun isMoveLeft(keyStroke: KeyStroke) =
        keyStroke.keyType == KeyType.ArrowLeft || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
            'a', 'A'
        )

    private fun isMoveDown(keyStroke: KeyStroke) =
        keyStroke.keyType == KeyType.ArrowDown || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
            's', 'S'
        )

    private fun isMoveRight(keyStroke: KeyStroke) =
        keyStroke.keyType == KeyType.ArrowRight || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
            'd', 'D'
        )

    private fun isMoveUp(keyStroke: KeyStroke) =
        keyStroke.keyType == KeyType.ArrowUp || keyStroke.keyType == KeyType.Character && keyStroke.character in listOf(
            'w', 'W'
        )

    override fun onUnhandledInput(
        basePane: Window?, keyStroke: KeyStroke?, hasBeenHandled: AtomicBoolean?
    ) {
        // not used
    }

    override fun onResized(window: Window?, oldSize: TerminalSize?, newSize: TerminalSize?) {
        // not used
    }

    override fun onMoved(
        window: Window?, oldPosition: TerminalPosition?, newPosition: TerminalPosition?
    ) {
        // not used
    }
}
