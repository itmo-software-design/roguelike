package com.github.itmosoftwaredesign.roguelike.app

import com.github.itmosoftwaredesign.roguelike.utils.vo.Consumable
import com.github.itmosoftwaredesign.roguelike.utils.vo.Position
import engine.GameSession
import messages.*
import messages.player.MoveDirection
import messages.player.MovePlayer
import messages.player.OpenInventory
import messages.player.PlayerInteract
import messages.ui.GameScreenExit
import ui.console.InventoryScreen
import java.util.concurrent.ConcurrentLinkedQueue

private const val maxStepsPerTick = 6

class GameLoop {
    @Volatile
    private var isRunning = true
    private val events = ConcurrentLinkedQueue<Message>()

    private var uiSubscriber = Subscriber {
        when (it) {
            is GameScreenExit -> isRunning = false
        }
    }
    private var playerSubscriber = Subscriber {
        events.add(it)
    }

    init {
        MessageBroker.subscribe(TOPIC_UI, uiSubscriber)
        MessageBroker.subscribe(TOPIC_PLAYER, playerSubscriber)
    }

    fun start() {
        println("Game loop started")

        try {
            while (isRunning) {
                handleInput()
                updateGameState()
            }
        } finally {
            MessageBroker.unsubscribe(TOPIC_UI, uiSubscriber)
            MessageBroker.unsubscribe(TOPIC_PLAYER, playerSubscriber)
            println("Game loop stopped")
        }
    }

    private fun handleInput() {
        //TODO: обработка инпута
        var step = 0
        while (events.isNotEmpty() && step < maxStepsPerTick) {
            val message = events.poll()
            val player = GameSession.player
            when (message) {
                is MovePlayer -> {
                    val newPosition = when (message.direction) {
                        MoveDirection.DOWN -> Position(player.position.x, player.position.y + 1)
                        MoveDirection.UP -> Position(player.position.x, player.position.y - 1)
                        MoveDirection.LEFT -> Position(player.position.x - 1, player.position.y)
                        MoveDirection.RIGHT -> Position(player.position.x + 1, player.position.y)
                    }

                    if (canGoTo(newPosition)) {
                        player.position = newPosition
                    }

                    step += 1
                }

                is PlayerInteract -> {
                    println("Interact with specific entity if possible")
                    player.inventory.addItem(Consumable("test", "test description", "damage"))
                }

                is OpenInventory -> {
                    InventoryScreen(player.inventory)
                }
            }
        }
    }

    private fun canGoTo(newPosition: Position): Boolean {
        val tileMap = GameSession.currentLevel.tiles
        if (tileMap.isEmpty() || tileMap[0].isEmpty()) {
            return false
        }

        val tileToGo = tileMap[newPosition.x][newPosition.y]
        return !tileToGo.type.blocked
    }

    private fun updateGameState() {
        // TODO: Логика обновления игры
    }

    fun stop() {
        isRunning = false
    }
}
