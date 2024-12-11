package ui.console.game

import com.googlecode.lanterna.gui2.*
import engine.GameSession
import messages.MessageBroker
import messages.TOPIC_UI
import messages.ui.GameScreenOpened
import ui.console.RenderContext
import ui.console.UIContext
import ui.localize.localize

class GameMapScreen(
    window: Window,
    uiContext: UIContext,
    onReturn: () -> Unit
) : Panel() {
    private val gameMapListener: GameMapListener
    private val oldHints = window.hints
    private val playerLabel = Label("")

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)
        fillColorOverride = RenderContext.backgroundColor

        window.setHints(
            setOf(
                Window.Hint.EXPANDED,
                Window.Hint.FULL_SCREEN,
                Window.Hint.NO_DECORATIONS,
            )
        )

        playerLabel.text = "game.screen.title".localize(uiContext.playerName)
        addComponent(playerLabel)

        val gameMapPanel = Panel()
        gameMapPanel.setRenderer(GameMapPanelRenderer())
        addComponent(gameMapPanel)

        addComponent(Label("game.screen.hint.inventory".localize()))
        addComponent(Label("game.screen.hint.interact".localize()))
        addComponent(Label("game.screen.hint.move".localize()))
        addComponent(Label("game.screen.hint.exit".localize()))

        gameMapListener = GameMapListener()
        window.addWindowListener(gameMapListener)

        gameMapListener.onClosed = {
            window.removeWindowListener(gameMapListener)
            window.setHints(oldHints)
            onReturn()
        }

        MessageBroker.send(TOPIC_UI, GameScreenOpened(uiContext.playerName))

        window.component = this
    }

    override fun onRemoved(container: Container?) {
        super.onRemoved(container)
    }

}
