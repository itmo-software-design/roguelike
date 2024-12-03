package ui.localize

object LocalizationSource {

    private val builtInLocalizations = mapOf(
        "title.welcome" to "Welcome to our Rogue Like!",
        "input.player-name" to "Player Name:",

        "title.inventory" to "Inventory",

        "text.play" to "Play",
        "text.use" to "Use",
        "text.info" to "Info",
        "text.drop" to "Drop",
        "text.close" to "Close",

        "title.item.info" to "Info - %s",
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
