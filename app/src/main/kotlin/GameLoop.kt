package com.github.itmosoftwaredesign.roguelike.app

import com.github.itmosoftwaredesign.roguelike.utils.vo.Consumable
import com.github.itmosoftwaredesign.roguelike.utils.vo.Player
import com.github.itmosoftwaredesign.roguelike.utils.vo.Position
import engine.GameSession
import messages.*
import messages.player.MoveDirection
import messages.player.MovePlayer
import messages.player.OpenInventory
import messages.player.PlayerInteract
import messages.ui.GameScreenExit
import ui.console.InventoryScreen
import vo.tile.PlayerTile
import java.util.concurrent.ConcurrentLinkedQueue

private const val maxStepsPerTick = 6

class GameLoop(private var player: Player) {
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
            stayPlayerOnTile()
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

    private fun stayPlayerOnTile() {
        val x = player.position.x
        val y = player.position.y
        val playerTile = PlayerTile(GameSession.tileMap[y][x])
        GameSession.tileMap[y][x] = playerTile
    }

    private fun removePlayerFromTile(x: Int, y: Int) {
        val tile = GameSession.tileMap[y][x]
        if (tile is PlayerTile) {
            GameSession.tileMap[y][x] = tile.stayOnTile
        }
    }

    private fun handleInput() {
        //TODO: обработка инпута
        var step = 0
        while (events.isNotEmpty() && step < maxStepsPerTick) {
            val message = events.poll()
            when (message) {
                is MovePlayer -> {
                    removePlayerFromTile(player.position.x, player.position.y)

                    when (message.direction) {
                        MoveDirection.DOWN -> {
                            val newPosition = Position(player.position.x, player.position.y + 1)
                            if (canGoTo(newPosition)) {
                                player.position = newPosition
                            }
                        }

                        MoveDirection.UP -> {
                            val newPosition = Position(player.position.x, player.position.y - 1)
                            if (canGoTo(newPosition)) {
                                player.position = newPosition
                            }
                        }

                        MoveDirection.LEFT -> {
                            val newPosition = Position(player.position.x - 1, player.position.y)
                            if (canGoTo(newPosition)) {
                                player.position = newPosition
                            }
                        }

                        MoveDirection.RIGHT -> {
                            val newPosition = Position(player.position.x + 1, player.position.y)
                            if (canGoTo(newPosition)) {
                                player.position = newPosition
                            }
                        }
                    }

                    stayPlayerOnTile()
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
        val tileMap = GameSession.tileMap
        if (tileMap.isEmpty() || tileMap[0].isEmpty()) return false

        if (newPosition.x < 0) return false
        if (newPosition.y < 0) return false
        if (newPosition.x >= tileMap[0].size) return false
        if (newPosition.y >= tileMap.size) return false

        val tile = tileMap[newPosition.y][newPosition.x]
        return tile.walkable
    }

    private fun updateGameState() {
        // TODO: Логика обновления игры
    }

    fun stop() {
        isRunning = false
    }
}
