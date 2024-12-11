package ui.localize

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
class LocalizationSourceTest {

    @Test
    fun `try to localize existing key`() {
        val actual = "text.play".localize()
        assertEquals("Play", actual)
    }

    @Test
    fun `try to localize existing key with substitution`() {
        val name = UUID.randomUUID().toString()
        val actual = "title.item.info".localize(name)
        assertEquals("Info - $name", actual)
    }

    @Test
    fun `try to localize not existing key`() {
        val name = UUID.randomUUID().toString()
        val actual = name.localize()
        assertEquals(name, actual)
    }

}
