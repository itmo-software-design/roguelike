package ui.localize

object LocalizationSource {

    private val builtInLocalizations = mapOf(
        "title.main-menu" to "Main Menu",
        "title.player-creation" to "Player Creation",
        "title.welcome" to "Welcome to our Rogue Like!\nIntroduce yourself, wanderer, what is your name?",
        "title.game.exit" to "Are you sure you want to exit?",
        "input.player-name" to "Player Name:",

        "title.inventory" to "Inventory",

        "text.play" to "Play",
        "text.exit" to "Exit",
        "text.next" to "Next",
        "text.back" to "Back",
        "text.use" to "Use",
        "text.info" to "Info",
        "text.drop" to "Drop",
        "text.close" to "Close",

        "title.item.info" to "Info - %s",

        "game.screen.title" to "The Tail of %s",

        "game.screen.hint.inventory" to "Press I to open the inventory",
        "game.screen.hint.interact" to "Press E to interact",
        "game.screen.hint.move" to "Press WASD or Arrows to move",
        "game.screen.hint.exit" to "Press Esc to exit",
    )

    /**
     * Get localization by code and format with passed substitutions
     */
    fun getLocalization(code: String, vararg args: String): String {
        val localization = builtInLocalizations[code] ?: code
        if (args.isEmpty()) {
            return localization
        }
        return localization.format(*args)
    }

}

fun String.localize(vararg args: String): String {
    return LocalizationSource.getLocalization(this, *args)
}
