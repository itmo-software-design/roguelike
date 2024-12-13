package ui.console

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

/**
 * The screen showing the inventory items and player characteristics.
 *
 * @author gkashin
 * @since 0.0.1
 */
class InventoryPlayerInfoScreen(
    inventoryPanel: InventoryScreen,
    playerInfoPanel: PlayerInfoScreen
) : Panel() {
    private val closeButton: Button = Button("x", this::onButtonClick)
    private val containerPanel = Panel()
    private val window = BasicWindow()

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(closeButton)
        addComponent(EmptySpace(TerminalSize(0, 0)))
        addComponent(containerPanel)
        containerPanel.layoutManager = LinearLayout(Direction.HORIZONTAL)
        containerPanel.addComponent(inventoryPanel)
        containerPanel.addComponent(playerInfoPanel)
        inventoryPanel.registerOnMenuUpdateCallback { playerInfoPanel.updateLabels() }

        window.setHints(
            setOf(
                Window.Hint.CENTERED
            )
        )
        window.component = this

        RenderContext.gui.addWindowAndWait(window)
    }

    private fun onButtonClick() {
        closeButton.isEnabled = false
        window.close()
    }
}

