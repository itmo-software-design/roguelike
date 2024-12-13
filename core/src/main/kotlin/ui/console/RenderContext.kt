package ui.console

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.MultiWindowTextGUI
import com.googlecode.lanterna.screen.Screen

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
object RenderContext {
    lateinit var screen: Screen
    lateinit var gui: MultiWindowTextGUI
    val backgroundColor = TextColor.ANSI.BLUE
}
