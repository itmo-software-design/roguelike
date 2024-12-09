package ui.console

import com.github.itmosoftwaredesign.roguelike.utils.vo.*
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.dialogs.MessageDialog
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.gui2.menu.Menu
import com.googlecode.lanterna.gui2.menu.MenuItem
import ui.localize.localize
import kotlin.math.max

/**
 *  The screen showing the inventory items of the Player
 * @author sibmaks
 * @since 0.0.1
 */
class InventoryScreen(
    inventory: Inventory,
) : Panel() {
    private val containerPanel = Panel()
    private var onMenuUpdateCallback: (() -> Unit)? = null

    init {
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(EmptySpace(TerminalSize(0, 0)))

        addComponent(
            containerPanel.withBorder(
                Borders.singleLine("title.inventory".localize())
            )
        )
        containerPanel.layoutManager = GridLayout(3)

        containerPanel.removeAllComponents()
        for (item in inventory) {
            val itemMenu = Menu(item.name)
            buildItemMenu(inventory, item, itemMenu)
            containerPanel.addComponent(itemMenu)
        }
    }

    /**
     * Registers callback function to be executed when Inventory menu is updated
     */
    fun registerOnMenuUpdateCallback(callback: () -> Unit) {
        onMenuUpdateCallback = callback
    }

    private fun buildItemMenu(
        inventory: Inventory,
        item: Item,
        menu: Menu,
    ) {
        when (item) {
            is Weapon -> {
                menu.add(
                    MenuItem("text.use".localize()) {
                        inventory.removeItem(item)
                        inventory.equipItem(item)
                        updateMenuItem(menu)
                    }
                )
            }
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
                }
            )

        onMenuUpdateCallback?.let { it() }
    }
}

