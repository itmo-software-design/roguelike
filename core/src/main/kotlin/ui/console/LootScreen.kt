package ui.console

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*


class LootScreen(val onClose: () -> Unit) : Panel() {
    private val closeButton: Button = Button("x", this::onButtonClick)

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(closeButton)
        addComponent(EmptySpace(TerminalSize(0, 0)))

        val containerPanel = Panel()
        addComponent(containerPanel)
        containerPanel.layoutManager = LinearLayout(Direction.HORIZONTAL)

        val leftPanel = Panel()
        containerPanel.addComponent(leftPanel.withBorder(Borders.singleLine("Your inventory")))

        leftPanel.layoutManager = GridLayout(5)
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))
        leftPanel.addComponent(Button("_"))

        val rightPanel = Panel()
        containerPanel.addComponent(rightPanel.withBorder(Borders.singleLine("Other inventory")))

        rightPanel.layoutManager = GridLayout(5)
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("x"))
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("_"))
        rightPanel.addComponent(Button("_"))
    }

    private fun onButtonClick() {
        closeButton.isEnabled = false
        onClose()
    }
}
