package ui.console

import com.googlecode.lanterna.gui2.*
import ui.localize.localize

class MainMenuScreen(private var window: Window) : Panel() {
    private val playButton: Button = Button("text.play".localize(), this::onPlayButton)
    private val exitButton: Button = Button("text.exit".localize(), this::onExitButton)

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(playButton)
        addComponent(Separator(Direction.HORIZONTAL))
        addComponent(exitButton)

        window.component = withBorder(
            Borders.singleLine("title.main-menu".localize())
        )
    }

    private fun onPlayButton() {
        playButton.isEnabled = false
        exitButton.isEnabled = false
        PlayerNameScreen(window) {
            MainMenuScreen(window)
        }
    }

    private fun onExitButton() {
        playButton.isEnabled = false
        exitButton.isEnabled = false
        RenderContext.screen.close()
    }
}
