package ui.console

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import ui.localize.localize

object MainMenuScreen : Panel() {
    private lateinit var window: Window
    private val playButton: Button = Button("text.play".localize(), this::onPlayButton)
    private val exitButton: Button = Button("text.exit".localize(), this::onExitButton)

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(playButton)
        addComponent(EmptySpace(TerminalSize(0, 0)))
        addComponent(exitButton)
    }

    fun show(window: Window) {
        this.window = window
        window.component = withBorder(
            Borders.singleLine("title.main-menu".localize())
        )
    }

    private fun onPlayButton() {
        playButton.isEnabled = false
        exitButton.isEnabled = false
        PlayerNameScreen.show(window, { println("Start game") }) {
            show(window)
        }
    }

    private fun onExitButton() {
        playButton.isEnabled = false
        exitButton.isEnabled = false
        window.close()
    }
}
