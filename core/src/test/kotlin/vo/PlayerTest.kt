package vo

import com.github.itmosoftwaredesign.roguelike.utils.vo.Inventory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PlayerTest {
    private lateinit var player: Player
    private lateinit var inventory: Inventory
    private lateinit var experienceManager: ExperienceManager

    private val MAX_HEALTH = 100
    private val BASE_ATTACK = 10
    private val BASE_DEFENSE = 10

    @BeforeEach
    fun setUp() {
        inventory = Inventory()
        experienceManager = ExperienceManager()
        player = Player("", MAX_HEALTH, BASE_ATTACK, BASE_DEFENSE, Position(0, 0), inventory=inventory, experienceManager=experienceManager)
    }

    @Test
    fun `should return correct values as defined in experience manager`() {
        assert(player.level == experienceManager.currentLevel)
        assert(player.currentPoints == experienceManager.currentPoints)

        for (level in 1..ExperienceManagerConstants.MAX_LEVEL) {
            assert(player.pointsToNextLevel == experienceManager.pointsToNextLevel)

            val pointsToAdd = experienceManager.pointsToNextLevel
            player.addExperience(pointsToAdd)

            assert(player.level == experienceManager.currentLevel)
            assert(player.currentPoints == experienceManager.currentPoints)
        }
    }

    @Test
    fun `should apply level boosts`() {
        for (level in 1..ExperienceManagerConstants.MAX_LEVEL) {
            assert(player.maxHealth == MAX_HEALTH + experienceManager.healthBoost)
            assert(player.defense == BASE_DEFENSE + experienceManager.defenceBoost)
            assert(player.attack == BASE_ATTACK + experienceManager.attackBoost)

            val pointsToAdd = experienceManager.pointsToNextLevel
            player.addExperience(pointsToAdd)
        }
    }

    @Test
    fun `items from inventory should increase attack and defense`() {
        val weaponDamage = 10
        val armorDefense = 20
        inventory.equipItem(Weapon("", "", weaponDamage))
        inventory.equipItem(Armor("", "", armorDefense))

        assert(player.attack == experienceManager.attackBoost + weaponDamage)
        assert(player.defense == experienceManager.defenceBoost + armorDefense)
    }
}
