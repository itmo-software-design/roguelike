package com.github.itmosoftwaredesign.roguelike.app

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal
import ui.console.MainMenuScreen
import java.io.IOException
import java.util.concurrent.TimeUnit


fun main() {
    val defaultTerminalFactory = DefaultTerminalFactory()
    val terminalSize = TerminalSize(180, 60)
    var terminal: Terminal? = null
    try {
        terminal = defaultTerminalFactory.createTerminal()
        while (!checkTerminalSize(terminal.terminalSize, terminalSize, terminal)) {
            TimeUnit.MILLISECONDS.sleep(100)
        }
        val screen: Screen = TerminalScreen(terminal)
        screen.startScreen()

        // Create window to hold the panel
        val window = BasicWindow()
        window.setHints(
            setOf(
                Window.Hint.CENTERED
            )
        )

        MainMenuScreen.show(window)

        val gui =
            MultiWindowTextGUI(
                screen,
                DefaultWindowManager(terminalSize),
                EmptySpace(TextColor.ANSI.BLUE)
            )
        gui.addWindowAndWait(window)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            terminal?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

private fun checkTerminalSize(
    actual: TerminalSize,
    required: TerminalSize,
    terminal: Terminal
): Boolean {
    if (actual.columns >= required.columns && actual.rows >= required.rows) {
        return true
    }
    terminal.clearScreen()
    terminal.setBackgroundColor(TextColor.ANSI.BLACK)
    terminal.setForegroundColor(TextColor.ANSI.RED)
    terminal.putString(
        "Too small size, " +
            "required at least ${required.columns} columns, ${required.rows} rows, " +
            "actual ${actual.columns} columns, ${actual.rows} rows"
    )
    terminal.flush()
    return false
}
