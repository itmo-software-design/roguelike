package ui.console


import com.googlecode.lanterna.gui2.Border
import com.googlecode.lanterna.gui2.Panel
import com.googlecode.lanterna.gui2.menu.Menu
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import vo.Inventory
import vo.Item
import vo.Weapon
import kotlin.test.assertEquals

/**
 * @author sibmaks
 * @since 0.0.1
 */
class InventoryScreenTest {

    private lateinit var mockInventory: Inventory
    private lateinit var inventoryScreen: InventoryScreen

    @BeforeEach
    fun setUp() {
        mockInventory = mockk(relaxed = true)
        every { mockInventory.iterator() } returns listOf<Item>(
            mockk(relaxed = true) {
                every { name } returns "Sword"
                every { description } returns "A sharp blade."
            },
            mockk<Weapon>(relaxed = true) {
                every { name } returns "Bow"
                every { description } returns "A long-range weapon."
            }
        ).iterator()

        inventoryScreen = InventoryScreen(mockInventory)
    }

    @Test
    fun `initialization sets up components correctly`() {
        val borderPanel = (this.inventoryScreen.children.elementAt(1) as Border)
        val containerPanel = borderPanel.children.first() as Panel
        assertEquals(
            2,
            containerPanel.children.filterIsInstance<Menu>().size
        )
    }

    @Test
    fun `menu update callback is triggered`() {
        val callback = mockk<() -> Unit>(relaxed = true)
        inventoryScreen.registerOnMenuUpdateCallback(callback)

        // Simulate menu update
        val mockMenu = mockk<Menu>(relaxed = true)
        inventoryScreen.javaClass.getDeclaredMethod("updateMenuItem", Menu::class.java)
            .apply { isAccessible = true }
            .invoke(inventoryScreen, mockMenu)

        verify { callback.invoke() }
    }
}
