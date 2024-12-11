package ui.console

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.MultiWindowTextGUI
import com.googlecode.lanterna.screen.Screen

/**
 * Game rendering context
 * @author sibmaks
 * @since 0.0.1
 */
object RenderContext {
    /**
     * Current game screen
     */
    lateinit var screen: Screen

    /**
     * Active game GUI
     */
    lateinit var gui: MultiWindowTextGUI

    /**
     * Default background color
     */
    val backgroundColor = TextColor.ANSI.BLUE
}
