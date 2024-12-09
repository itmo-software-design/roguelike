package com.github.itmosoftwaredesign.roguelike.app

import com.github.itmosoftwaredesign.roguelike.utils.vo.Armor
import com.github.itmosoftwaredesign.roguelike.utils.vo.Consumable
import com.github.itmosoftwaredesign.roguelike.utils.vo.Position
import com.github.itmosoftwaredesign.roguelike.utils.vo.Weapon
import com.github.itmosoftwaredesign.roguelike.utils.vo.*
import engine.GameSession
import messages.*
import messages.player.MoveDirection
import messages.player.MovePlayer
import messages.player.OpenInventory
import messages.player.PlayerInteract
import messages.ui.GameScreenExit
import ui.console.InventoryPlayerInfoScreen
import ui.console.InventoryScreen
import ui.console.PlayerInfoScreen
import ui.console.RenderContext
import vo.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

private const val maxStepsPerTick = 6

class GameLoop {
    private val isRunning = AtomicBoolean()
    private val events = ConcurrentLinkedQueue<Message>()

    private var uiSubscriber = Subscriber {
        when (it) {
            is GameScreenExit -> isRunning.set(false)
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
        isRunning.set(true)

        try {
            while (isRunning.get()) {
                handleInput()
                updateGameState()
                RenderContext.gui.guiThread.processEventsAndUpdate()
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
                    val direction: MoveDirection
                    val newPosition = when (message.direction) {
                        MoveDirection.DOWN -> {
                            direction = MoveDirection.DOWN
                            Position(player.position.x, player.position.y + 1)
                        }

                        MoveDirection.UP -> {
                            direction = MoveDirection.UP
                            Position(player.position.x, player.position.y - 1)
                        }

                        MoveDirection.LEFT -> {
                            direction = MoveDirection.LEFT
                            Position(player.position.x - 1, player.position.y)
                        }

                        MoveDirection.RIGHT -> {
                            direction = MoveDirection.RIGHT
                            Position(player.position.x + 1, player.position.y)
                        }
                    }

                    player.direction =
                        direction // куда направлен direction, в ту сторону будет производиться взаимодействие
                    if (canGoTo(newPosition)) {
                        player.position = newPosition
                    }
                    step += 1
                }

                is PlayerInteract -> {
                    println("Interact with specific entity if possible")
                    tryInteract(player.position, player.direction)
                }

                is OpenInventory -> {
                    InventoryPlayerInfoScreen(
                        InventoryScreen(player.inventory),
                        PlayerInfoScreen(player)
                    )
                }
            }
        }
    }

    private fun canGoTo(newPosition: Position): Boolean {
        val tileMap = GameSession.currentDungeonLevel.tiles
        if (tileMap.isEmpty() || tileMap[0].isEmpty()) {
            return false
        }

        val tileToGo = tileMap[newPosition.x][newPosition.y]
        return !tileToGo.type.blocked
    }

    private fun tryInteract(position: Position, direction: MoveDirection) {
        when (direction) {
            MoveDirection.DOWN -> {
                tryInteractAt(Position(position.x, position.y + 1))
            }

            MoveDirection.UP -> {
                tryInteractAt(Position(position.x, position.y - 1))
            }

            MoveDirection.LEFT -> {
                tryInteractAt(Position(position.x - 1, position.y))
            }

            MoveDirection.RIGHT -> {
                tryInteractAt(Position(position.x + 1, position.y))
            }
        }
    }

    private fun tryInteractAt(position: Position) {
        val tileType = GameSession.currentDungeonLevel.tiles[position.x][position.y].type

        when (tileType) {
            TileType.PORTAL -> {
                GameSession.moveToNextLevel()
            }

            TileType.CONSUMABLE -> {
                GameSession.player.inventory.addItem(
                    Consumable(
                        "Зелье",
                        "Убивает на раз",
                        "damage"
                    )
                )
                GameSession.currentDungeonLevel.tiles[position.x][position.y].type = TileType.FLOOR
            }

            TileType.WEAPON -> {
                GameSession.player.inventory.addItem(
                    _root_ide_package_.vo.Weapon(
                        "Меч-гладенец",
                        "Острый",
                        10
                    )
                )
                GameSession.currentDungeonLevel.tiles[position.x][position.y].type = TileType.FLOOR
            }

            TileType.ARMOR -> {
                GameSession.player.inventory.addItem(
                    _root_ide_package_.vo.Armor(
                        "Шлем рыцаря",
                        "Крепкий",
                        10
                    )
                )
                GameSession.currentDungeonLevel.tiles[position.x][position.y].type = TileType.FLOOR
            }

            TileType.MOB -> {
                GameSession.player.health -= 10
                GameSession.player.addExperience(10)
                GameSession.currentDungeonLevel.tiles[position.x][position.y].type = TileType.FLOOR
            }

            else -> {
                // todo
            }
        }
    }

    private fun updateGameState() {
        // TODO: Логика обновления игры
    }

    fun stop() {
        isRunning.set(false)
    }
}
