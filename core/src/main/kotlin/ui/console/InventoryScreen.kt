package ui.console

import com.github.itmosoftwaredesign.roguelike.utils.vo.Consumable
import com.github.itmosoftwaredesign.roguelike.utils.vo.Inventory
import com.github.itmosoftwaredesign.roguelike.utils.vo.Item
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.dialogs.MessageDialog
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.gui2.menu.Menu
import com.googlecode.lanterna.gui2.menu.MenuBar
import com.googlecode.lanterna.gui2.menu.MenuItem
import ui.localize.localize
import kotlin.math.max


object InventoryScreen : Panel() {
    private val closeButton: Button = Button("x", this::onButtonClick)
    private val containerPanel = Panel()
    private var onClose: () -> Unit = {}

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

    }

    fun show(inventory: Inventory, onClose: () -> Unit) {
        this.onClose = onClose
        containerPanel.removeAllComponents()
        for (item in inventory) {
            val itemButton = Button(item.name)
            val itemMenuBar = MenuBar()
            val itemMenu = buildItemMenu(inventory, item, itemMenuBar)
            itemMenuBar.add(itemMenu)
            containerPanel.addComponent(itemButton)
        }
    }

    private fun buildItemMenu(
        inventory: Inventory,
        item: Item,
        itemMenuBar: MenuBar,
    ): Menu {
        val itemMenu = Menu(item.name)
        if (item is Consumable) {
            itemMenu.add(MenuItem("text.use".localize()) {
                inventory.removeItem(item)
                item.consume()
                updateMenuItem(itemMenuBar, itemMenu)
            })
        }
        itemMenu.add(MenuItem("text.info".localize()) {
            MessageDialog.showMessageDialog(
                textGUI as WindowBasedTextGUI,
                "title.item.info".localize(item.name),
                item.description,
                MessageDialogButton.OK
            )
        })
        itemMenu.add(MenuItem("text.drop".localize()) {
            inventory.removeItem(item)
            updateMenuItem(itemMenuBar, itemMenu)
        })
        itemMenu.add(MenuItem("text.close".localize()))
        return itemMenu
    }

    private fun updateMenuItem(
        itemMenuBar: MenuBar,
        itemMenu: Menu
    ) {
        itemMenuBar.removeComponent(itemMenu)
        val children = containerPanel.children
        if (children.isEmpty() || children.size == 1) {
            containerPanel.removeComponent(itemMenuBar)
            closeButton.takeFocus()
        } else {
            var focusedIndex = 0
            for ((index, component) in children.withIndex()) {
                if (component == itemMenuBar) {
                    focusedIndex = index - 1
                    break
                }
            }
            focusedIndex = max(0, focusedIndex)
            containerPanel.removeComponent(itemMenuBar)
            children.stream()
                .skip(focusedIndex.toLong())
                .filter { it is MenuBar }
                .map { it as MenuBar }
                .map { it.children.iterator() }
                .filter { it.hasNext() }
                .map { it.next() }
                .map { it as Interactable }
                .findAny()
                .ifPresentOrElse(
                    {
                        val interactable = it as Interactable
                        interactable.takeFocus()
                    },
                    {
                        containerPanel.removeComponent(itemMenuBar)
                        closeButton.takeFocus()
                    }
                )
        }
    }

    private fun onButtonClick() {
        closeButton.isEnabled = false
        onClose()
    }
}

