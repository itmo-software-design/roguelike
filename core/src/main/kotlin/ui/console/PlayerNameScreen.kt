package ui.console

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import ui.localize.localize

class PlayerNameScreen(val onNext: (playerName: String) -> Unit) : Panel() {
    private val playerTextBox: TextBox = TextBox()
    private val goButton: Button = Button("text.play".localize())

    init {
        layoutManager = GridLayout(2)

        addComponent(Label("title.welcome".localize()))
        addComponent(EmptySpace(TerminalSize(0, 0)))

        addComponent(Label("input.player-name".localize()))
        addComponent(playerTextBox)

        addComponent(EmptySpace(TerminalSize(0, 0)))
        goButton.addListener(this::onButtonClick)
        addComponent(goButton)
    }

    private fun onButtonClick(button: Button) {
        button.isEnabled = false
        val text = playerTextBox.text
        if (text.isNotBlank()) {
            onNext(text)
        }
    }
}
