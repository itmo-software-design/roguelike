package vo

import com.github.itmosoftwaredesign.roguelike.utils.vo.Inventory
import messages.player.MoveDirection

/**
 * Класс игрока
 */
class Player(
    name: String,
    maxHealth: Int,
    baseAttack: Int,
    baseDefense: Int,
    position: Position,
    direction: MoveDirection = MoveDirection.UP,
    var inventory: Inventory = Inventory(),
    private var experienceManager: ExperienceManager = ExperienceManager()
) : Character(maxHealth, baseAttack, baseDefense, position, direction) {

    /**
     * Name of the Player.
     */
    var name: String = name
        private set

    /**
     * Current level of the Player.
     */
    val level: Int
        get() = experienceManager.currentLevel

    /**
     * Number of points currently achieved.
     */
    val currentPoints: Int
        get() = experienceManager.currentPoints

    /**
     * Total number of points required to get to the next level.
     */
    val pointsToNextLevel: Int
        get() = experienceManager.pointsToNextLevel

    /**
     * Total amount of health consisting of base health and the level boost.
     */
    override val maxHealth: Int
        get() = experienceManager.healthBoost + super.maxHealth

    /**
     * Total amount of attack consisting of the level boost and the attack of the Player.
     */
    override val attack: Int
        get() = experienceManager.attackBoost + (inventory.getEquippedWeapon()?.damage ?: super.attack)

    /**
     * Total amount of defense consisting of the level boost and the defense of the Player.
     */
    override val defense: Int
        get() = experienceManager.defenceBoost + (inventory.getEquippedArmor()?.defense ?: super.defense)

    /**
     * Add points of experience to the Player.
     */
    fun addExperience(points: Int) {
        experienceManager.addExperience(points)
    }
}
