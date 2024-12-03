package ui.console

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import ui.localize.localize

object PlayerNameScreen : Panel() {
    private val playerTextBox: TextBox = TextBox()
    private val nextButton: Button = Button("text.play".localize(), this::onNextButtonClick)
    private val returnButton: Button = Button("text.back".localize(), this::onReturnButtonClick)
    private var onReturn: () -> Unit = {}

    init {
        layoutManager = GridLayout(2)

        addComponent(Label("title.welcome".localize()))
        addComponent(EmptySpace(TerminalSize(0, 0)))

        addComponent(Label("input.player-name".localize()))
        addComponent(playerTextBox)

        addComponent(EmptySpace(TerminalSize(0, 0)))
        addComponent(returnButton)

        addComponent(EmptySpace(TerminalSize(0, 0)))
        addComponent(nextButton)
    }

    fun show(window: Window, onReturn: () -> Unit) {
        playerTextBox.text = ""
        this.onReturn = onReturn
        window.component = withBorder(
            Borders.singleLine("title.player-creation".localize())
        )
    }

    private fun onNextButtonClick() {
        nextButton.isEnabled = false
        val text = playerTextBox.text
        if (text.isNotBlank()) {
            //onNext(text)
        } else {
            nextButton.isEnabled = true
        }
    }

    private fun onReturnButtonClick() {
        returnButton.isEnabled = false
        onReturn()
    }
}
