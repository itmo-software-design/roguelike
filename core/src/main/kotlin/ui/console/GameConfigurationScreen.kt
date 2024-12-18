package ui.console

import com.googlecode.lanterna.gui2.*
import ui.console.game.GameMapScreen
import ui.localize.localize

class GameConfigurationScreen(
    private var window: Window,
    private var onReturn: () -> Unit
) : Panel() {
    private val playerTextBox: TextBox = TextBox()
    private val fileTextBox: TextBox = TextBox()
    private val nextButton: Button = Button("text.play".localize(), this::onNextButtonClick)
    private val returnButton: Button = Button("text.back".localize(), this::onReturnButtonClick)

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(Label("title.welcome".localize()))

        addComponent(Separator(Direction.HORIZONTAL))

        val formPanel = Panel()
        formPanel.layoutManager = GridLayout(2)
        addComponent(formPanel)

        formPanel.addComponent(Label("input.player-name".localize()))
        formPanel.addComponent(playerTextBox)
        formPanel.addComponent(EmptySpace())
        formPanel.addComponent(EmptySpace())
        formPanel.addComponent(Label("input.file-name".localize()))
        formPanel.addComponent(fileTextBox)

        addComponent(Separator(Direction.HORIZONTAL))

        val buttonPanel = Panel()
        addComponent(buttonPanel)

        buttonPanel.layoutManager = GridLayout(2)
        buttonPanel.addComponent(returnButton)
        buttonPanel.addComponent(nextButton)

        window.component = withBorder(
            Borders.singleLine("title.player-creation".localize())
        )
    }

    private fun onNextButtonClick() {
        nextButton.isEnabled = false
        val playerName = playerTextBox.text
        val fileName = fileTextBox.text
        if (playerName.isNotBlank()) {
            val uiContext = UIContext(playerName, fileName)
            GameMapScreen(window, uiContext) {
                MainMenuScreen(window)
            }
        } else {
            nextButton.isEnabled = true
        }
    }

    private fun onReturnButtonClick() {
        returnButton.isEnabled = false
        onReturn()
    }
}
