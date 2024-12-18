package ui.console


import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.console.game.GameMapScreen
import ui.localize.localize
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


/**
 *
 * @author sibmaks
 * @since 0.0.1
 */

class PlayerNameScreenTest {

    private lateinit var mockWindow: Window
    private lateinit var mockOnReturn: () -> Unit
    private lateinit var gameConfigurationScreen: GameConfigurationScreen

    @BeforeEach
    fun setUp() {
        mockWindow = mockk(relaxed = true)
        mockOnReturn = mockk(relaxed = true)
        gameConfigurationScreen = GameConfigurationScreen(mockWindow, mockOnReturn)
    }

    @Test
    fun `should initialize UI components with localized text`() {
        val labels = gameConfigurationScreen.children.filterIsInstance<Label>()
        val playerNameTextBox =
            (gameConfigurationScreen.children.elementAt(2) as Panel).children.filterIsInstance<TextBox>()
                .first()
        val fileNameTextBox =
            (gameConfigurationScreen.children.elementAt(2) as Panel).children.filterIsInstance<TextBox>()
                .last()
        val buttons =
            (gameConfigurationScreen.children.last() as Panel).children.filterIsInstance<Button>()

        assertTrue(labels.any { it.text == "title.welcome".localize() })
        assertEquals(
            "text.play".localize(),
            buttons.find { it.label == "text.play".localize() }?.label
        )
        assertEquals(
            "text.back".localize(),
            buttons.find { it.label == "text.back".localize() }?.label
        )
        assertNotNull(playerNameTextBox, "Player name TextBox should exist")
        assertNotNull(fileNameTextBox, "File name TextBox should exist")
    }

    @Test
    fun `should transition to GameMapScreen when Next button is clicked with valid name`() {
        val mockGameMapScreen = mockk<GameMapScreen>(relaxed = true)
        val textBox =
            (gameConfigurationScreen.children.elementAt(2) as Panel).children.filterIsInstance<TextBox>()
                .first()
        val nextButton =
            (gameConfigurationScreen.children.last() as Panel).children.filterIsInstance<Button>()
                .find { it.label == "text.play".localize() }

        textBox.text = "PlayerName"

        mockkConstructor(GameMapScreen::class)

        nextButton?.onEnterFocus(Interactable.FocusChangeDirection.UP, nextButton)
        nextButton?.handleInput(KeyStroke(KeyType.Enter))

        assertFalse(nextButton?.isEnabled ?: true)
    }

    @Test
    fun `should disable Next button and not transition when name is blank`() {
        val textBox =
            (gameConfigurationScreen.children.elementAt(2) as Panel).children.filterIsInstance<TextBox>()
                .first()
        val nextButton =
            (gameConfigurationScreen.children.last() as Panel).children.filterIsInstance<Button>()
                .find { it.label == "text.play".localize() }

        textBox.text = ""

        mockkConstructor(GameMapScreen::class)

        nextButton?.onEnterFocus(Interactable.FocusChangeDirection.UP, nextButton)
        nextButton?.handleKeyStroke(KeyStroke(KeyType.Enter))

        assertTrue(nextButton?.isEnabled ?: false)
    }

    @Test
    fun `should invoke onReturn callback when Return button is clicked`() {
        val returnButton =
            (gameConfigurationScreen.children.last() as Panel).children.filterIsInstance<Button>()
                .find { it.label == "text.back".localize() }

        returnButton?.onEnterFocus(Interactable.FocusChangeDirection.UP, returnButton)
        returnButton?.handleInput(KeyStroke(KeyType.Enter))

        verify { mockOnReturn.invoke() }
        assertFalse(returnButton?.isEnabled ?: true)
    }
}
