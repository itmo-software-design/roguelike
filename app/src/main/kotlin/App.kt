package com.github.itmosoftwaredesign.roguelike.app

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
import java.io.EOFException
import java.io.IOException
import java.util.concurrent.TimeUnit


fun main() {
    val defaultTerminalFactory = DefaultTerminalFactory()
    val terminalSize = TerminalSize(120, 100)
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

        var gameStarted = false
        MessageBroker.subscribe(TOPIC_UI) {
            when (it) {
                is GameScreenOpened -> {
                    GameSession.startNewGame(it.playerName, it.fileName)
                    gameStarted = true
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
        gui.addWindow(window)

        do {
            try {
                while (!gameStarted) {
                    gui.guiThread.processEventsAndUpdate()
                    TimeUnit.MILLISECONDS.sleep(50)
                }
                val gameLoop = GameLoop()
                gameLoop.start()
            } catch (e: EOFException) {
                break
            }
        } while (!Thread.interrupted())
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
