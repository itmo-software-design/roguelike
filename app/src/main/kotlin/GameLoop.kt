package com.github.itmosoftwaredesign.roguelike.app

import com.github.itmosoftwaredesign.roguelike.utils.vo.Player

class GameLoop(player: Player) {
    @Volatile private var isRunning = true

    fun start() {
        while (isRunning) {
            render()
            handleInput()
            updateGameState()
        }
    }

    private fun render() {
        // TODO: Вызов логики рендеринга
    }

    private fun handleInput() {
        //TODO: обработка инпута
    }

    private fun updateGameState() {
        // TODO: Логика обновления игры
    }

    fun stop() {
        isRunning = false
    }
}
