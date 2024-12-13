package com.github.itmosoftwaredesign.roguelike.app

import engine.GameSession
import engine.action.AttackAction
import engine.action.MoveAction
import engine.factory.MobManager
import io.github.oshai.kotlinlogging.KotlinLogging
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
        logger.info { "Game loop started" }
        isRunning.set(true)

        try {
            while (isRunning.get()) {
                if (!RenderContext.gui.guiThread.processEventsAndUpdate()) {
                    continue
                }

                val inputMessage = waitForInput()
                if (inputMessage != null) {
                    handleInput(inputMessage)
                    updateGameState()
                    RenderContext.gui.guiThread.processEventsAndUpdate()
                }
            }
        } finally {
            MessageBroker.unsubscribe(TOPIC_UI, uiSubscriber)
            MessageBroker.unsubscribe(TOPIC_PLAYER, playerSubscriber)
            logger.info { "Game loop stopped" }
        }
    }

    private fun waitForInput(): Message? {
        var step = 0
        while (events.isNotEmpty() && step < maxStepsPerTick) {
            val message = events.poll()
            if (message != null) {
                return message
            }

            step += 1
        }

        return null
    }

    private fun handleInput(message: Message) {
        val player = GameSession.player
        when (message) {
            is MovePlayer -> {
                MoveAction.perform(
                    player,
                    message.direction,
                    dungeonLevel = GameSession.currentDungeonLevel
                )
            }

            is PlayerInteract -> {
                logger.debug { "Interact with specific entity if possible" }
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

    private fun tryInteract(position: Position, direction: MoveDirection) {
        when (direction) {
            MoveDirection.DOWN -> {
                tryInteractAt(position.copy(y = position.y + 1))
            }

            MoveDirection.UP -> {
                tryInteractAt(position.copy(y = position.y - 1))
            }

            MoveDirection.LEFT -> {
                tryInteractAt(position.copy(x = position.x - 1))
            }

            MoveDirection.RIGHT -> {
                tryInteractAt(position.copy(x = position.x + 1))
            }
        }
    }

    private fun tryInteractAt(position: Position) {
        val mobToInteract = MobManager.getMobAt(GameSession.currentDungeonLevel, position)
        if (mobToInteract != null) {
            AttackAction.perform(GameSession.player, mobToInteract, GameSession.currentDungeonLevel)
            return
        }

        val tileToInteract = GameSession.currentDungeonLevel.getTileAt(position)
        when (tileToInteract.type) {
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
                tileToInteract.type = TileType.FLOOR
            }

            TileType.WEAPON -> {
                GameSession.player.inventory.addItem(
                    Weapon(
                        "Меч-гладенец",
                        "Острый",
                        10
                    )
                )
                tileToInteract.type = TileType.FLOOR
            }

            TileType.ARMOR -> {
                GameSession.player.inventory.addItem(
                    Armor(
                        "Шлем рыцаря",
                        "Крепкий",
                        10
                    )
                )
                tileToInteract.type = TileType.FLOOR
            }

            else -> {
                // todo
            }
        }
    }

    private fun updateGameState() {
        MobManager.getActiveMobs(GameSession.currentDungeonLevel).forEach {
            it.behaviour.act(it, GameSession.currentDungeonLevel, GameSession.player)
        }
    }

    fun stop() {
        isRunning.set(false)
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
