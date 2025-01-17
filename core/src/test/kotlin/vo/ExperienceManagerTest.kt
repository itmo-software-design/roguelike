package vo

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExperienceManagerTest {
    private lateinit var experienceManager: ExperienceManager

    @BeforeEach
    fun setUp() {
        experienceManager = ExperienceManager()
    }

    @Test
    fun `should increment level by 1 and have no points remaining`() {
        assert(experienceManager.currentLevel == 0)
        assert(experienceManager.currentPoints == 0)

        experienceManager.addExperience(experienceManager.pointsToNextLevel)

        assert(experienceManager.currentLevel == 1)
        assert(experienceManager.currentPoints == 0)
    }

    @Test
    fun `should increment level by 1 and carry over remaining points`() {
        assert(experienceManager.currentLevel == 0)
        assert(experienceManager.currentPoints == 0)

        val pointsToAdd = (1.5 * experienceManager.pointsToNextLevel).toInt()
        experienceManager.addExperience(pointsToAdd)

        assert(experienceManager.currentLevel == 1)
        assert(experienceManager.currentPoints == pointsToAdd / 3)
    }

    @Test
    fun `should not update level`() {
        assert(experienceManager.currentLevel == 0)
        assert(experienceManager.currentPoints == 0)

        val pointsToAdd = (0.5 * experienceManager.pointsToNextLevel).toInt()
        experienceManager.addExperience(pointsToAdd)

        assert(experienceManager.currentLevel == 0)
        assert(experienceManager.currentPoints == pointsToAdd)
    }

    @Test
    fun `should not go over max level`() {
        assert(experienceManager.currentLevel == 0)
        assert(experienceManager.currentPoints == 0)

        for (level in 1..ExperienceManagerConstants.MAX_LEVEL) {
            experienceManager.addExperience(experienceManager.pointsToNextLevel)
            assert(experienceManager.currentLevel == level)
        }
        val pointsToAddOverLastLevel = 10
        experienceManager.addExperience(pointsToAddOverLastLevel)

        assert(experienceManager.currentLevel == ExperienceManagerConstants.MAX_LEVEL)
        assert(experienceManager.currentPoints == pointsToAddOverLastLevel)
    }

    @Test
    fun `number of points to next level should depend on level`() {
        assert(experienceManager.currentLevel == 0)
        assert(experienceManager.currentPoints == 0)

        for (level in 1..ExperienceManagerConstants.MAX_LEVEL) {
            experienceManager.addExperience(experienceManager.pointsToNextLevel)
            assert(experienceManager.currentLevel == level)
            assert(experienceManager.currentPoints == 0)
        }
    }

    @Test
    fun `boosts should depend on level`() {
        assert(experienceManager.currentLevel == 0)
        assert(experienceManager.currentPoints == 0)

        for (level in 1..ExperienceManagerConstants.MAX_LEVEL) {
            experienceManager.addExperience(experienceManager.pointsToNextLevel)
            assert(experienceManager.healthBoost == level * ExperienceManagerConstants.HEALTH_POINTS_PER_LEVEL)
            assert(experienceManager.attackBoost == level * ExperienceManagerConstants.ATTACK_POINTS_PER_LEVEL)
            assert(experienceManager.defenceBoost == level * ExperienceManagerConstants.DEFENCE_POINTS_PER_LEVEL)
        }
    }
}
