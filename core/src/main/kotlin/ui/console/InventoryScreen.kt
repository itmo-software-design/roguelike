package ui.console

import com.github.itmosoftwaredesign.roguelike.utils.vo.Consumable
import com.github.itmosoftwaredesign.roguelike.utils.vo.Inventory
import com.github.itmosoftwaredesign.roguelike.utils.vo.Item
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.dialogs.MessageDialog
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.gui2.menu.Menu
import com.googlecode.lanterna.gui2.menu.MenuItem
import ui.localize.localize
import kotlin.math.max


class InventoryScreen(
    inventory: Inventory,
) : Panel() {
    private val closeButton: Button = Button("x", this::onButtonClick)
    private val containerPanel = Panel()
    private val window = BasicWindow()

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(closeButton)
        addComponent(EmptySpace(TerminalSize(0, 0)))

        addComponent(
            containerPanel.withBorder(
                Borders.singleLine("title.inventory".localize())
            )
        )
        containerPanel.layoutManager = GridLayout(5)

        containerPanel.removeAllComponents()
        for (item in inventory) {
            val itemMenu = Menu(item.name)
            buildItemMenu(inventory, item, itemMenu)
            containerPanel.addComponent(itemMenu)
        }

        window.setHints(
            setOf(
                Window.Hint.CENTERED
            )
        )
        window.component = this

        RenderContext.gui.addWindow(window)
    }

    private fun buildItemMenu(
        inventory: Inventory,
        item: Item,
        menu: Menu,
    ) {
        if (item is Consumable) {
            menu.add(
                MenuItem("text.use".localize()) {
                    inventory.removeItem(item)
                    item.consume()
                    updateMenuItem(menu)
                }
            )
        }
        menu.add(MenuItem("text.info".localize()) {
            MessageDialog.showMessageDialog(
                textGUI as WindowBasedTextGUI,
                "title.item.info".localize(item.name),
                item.description,
                MessageDialogButton.OK
            )
        })
        menu.add(MenuItem("text.drop".localize()) {
            inventory.removeItem(item)
            updateMenuItem(menu)
        })
        menu.add(MenuItem("text.close".localize()))
    }

    private fun updateMenuItem(menu: Menu) {
        val children = containerPanel.children
        if (children.isEmpty() || children.size == 1) {
            containerPanel.removeComponent(menu)
            closeButton.takeFocus()
            return
        }
        var focusedIndex = 0
        for ((index, component) in children.withIndex()) {
            if (component == menu) {
                focusedIndex = index - 1
                break
            }
        }
        focusedIndex = max(0, focusedIndex)
        containerPanel.removeComponent(menu)
        children.stream()
            .skip(focusedIndex.toLong())
            .filter { it is Menu }
            .map { it as Menu }
            .findAny()
            .ifPresentOrElse(
                {
                    it.takeFocus()
                },
                {
                    containerPanel.removeComponent(menu)
                    closeButton.takeFocus()
                }
            )
    }

    private fun onButtonClick() {
        closeButton.isEnabled = false
        window.close()
    }
}

