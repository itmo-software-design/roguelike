package vo

/**
 *  Class responsible for managing Player experience and levels.
 * @author gkashin
 * @since 0.0.1
 */
class ExperienceManager {
    /**
     * Current level of the Player.
     */
    var currentLevel = 0
        private set

    /**
     * Number of points currently achieved.
     * The remaining is carried over to the next level.
     */
    var currentPoints = 0
        private set

    /**
     * Total number of points required to get to the next level.
     */
    val pointsToNextLevel: Int
        get() = ExperienceManagerConstants.LEVEL_FACTOR * currentLevel + ExperienceManagerConstants.BASE_LEVEL_POINTS

    /**
     * Additional health depending on level.
     */
    val healthBoost: Int
        get() = currentLevel * ExperienceManagerConstants.HEALTH_POINTS_PER_LEVEL

    /**
     * Additional attack depending on level.
     */
    val attackBoost: Int
        get() = currentLevel * ExperienceManagerConstants.ATTACK_POINTS_PER_LEVEL

    /**
     * Additional defence depending on level.
     */
    val defenceBoost
        get() = currentLevel * ExperienceManagerConstants.DEFENCE_POINTS_PER_LEVEL

    /**
     * Adds points of experience and updates level if needed.
     */
    fun addExperience(points: Int) {
        currentPoints += points

        updateLevelIfNeeded()
    }

    private fun updateLevelIfNeeded() {
        if (currentPoints >= pointsToNextLevel) {
            currentPoints %= pointsToNextLevel
            currentLevel = minOf(currentLevel + 1, ExperienceManagerConstants.MAX_LEVEL)
        }
    }
}

object ExperienceManagerConstants {
    const val BASE_LEVEL_POINTS = 10 // base level of points in each level
    const val LEVEL_FACTOR = 2 // factor of points increase in each level
    const val MAX_LEVEL = 10
    const val HEALTH_POINTS_PER_LEVEL = 10
    const val ATTACK_POINTS_PER_LEVEL = 5
    const val DEFENCE_POINTS_PER_LEVEL = 3
}
