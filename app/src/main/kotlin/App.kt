package com.github.itmosoftwaredesign.roguelike.app

import com.github.itmosoftwaredesign.roguelike.utils.vo.Player
import com.github.itmosoftwaredesign.roguelike.utils.vo.Position
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal
import engine.GameSession
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
                    thread {
                        val player = Player(GameSession.playerName, 100, 1, 1, Position(1, 1))
                        val gameLoop = GameLoop(player)
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
