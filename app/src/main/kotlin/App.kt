package com.github.itmosoftwaredesign.roguelike.app

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal
import engine.GameSession
import engine.LevelGenerator
import messages.MessageBroker
import messages.TOPIC_UI
import messages.ui.GameScreenOpened
import ui.console.MainMenuScreen
import ui.console.RenderContext
import java.io.IOException
import kotlin.concurrent.thread


fun main() {
    val defaultTerminalFactory = DefaultTerminalFactory()
    val terminalSize = TerminalSize(120, 40)
    var terminal: Terminal? = null
    try {
        terminal = defaultTerminalFactory.createTerminal()

        val screen: Screen = TerminalScreen(terminal)
        RenderContext.screen = screen
        screen.startScreen()

        val window = BasicWindow()
        window.setHints(
            setOf(
                Window.Hint.CENTERED
            )
        )

        MessageBroker.subscribe(TOPIC_UI) {
            when (it) {
                is GameScreenOpened -> {
                    val firstLevel = LevelGenerator(42).generate()
                    GameSession.startNewGame(GameSession.playerName, firstLevel)
                    thread {
                        val gameLoop = GameLoop()
                        gameLoop.start()
                    }
                }
            }
        }

        MainMenuScreen(window)

        val gui = MultiWindowTextGUI(
            screen,
            DefaultWindowManager(terminalSize),
            EmptySpace(RenderContext.backgroundColor)
        )
        RenderContext.gui = gui
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
