package com.github.itmosoftwaredesign.roguelike.app

class GameLoop() {
    private var isRunning = true

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
}
