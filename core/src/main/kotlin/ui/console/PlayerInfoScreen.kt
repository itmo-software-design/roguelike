package ui.console

import com.googlecode.lanterna.gui2.*
import ui.localize.localize
import vo.ExperienceManagerConstants
import vo.Player

/**
 *  The screen showing the main characteristics of the Player
 * @author gkashin
 * @since 0.0.1
 */
class PlayerInfoScreen(
    private val player: Player,
) : Panel() {
    private val levelLabel = Label("")
    private val expLabel = Label("")
    private val maxHealthLabel = Label("")
    private val healthLabel = Label("")
    private val attackLabel = Label("")
    private val defenceLabel = Label("")

    init {
        val containerPanel = Panel()
        layoutManager = LinearLayout(Direction.VERTICAL)

        addComponent(
            containerPanel.withBorder(
                Borders.singleLine("title.player-info".localize())
            )
        )
        containerPanel.layoutManager = LinearLayout(Direction.VERTICAL)

        containerPanel.removeAllComponents()
        containerPanel.addComponent(levelLabel)
        containerPanel.addComponent(expLabel)
        containerPanel.addComponent(maxHealthLabel)
        containerPanel.addComponent(healthLabel)
        containerPanel.addComponent(attackLabel)
        containerPanel.addComponent(defenceLabel)

        updateLabels()
    }

    /**
     *  Update all labels with the new Player info.
     */
    fun updateLabels() {
        levelLabel.text = "Level: ${player.level}/${ExperienceManagerConstants.MAX_LEVEL}"
        expLabel.text = "Exp.: ${player.currentPoints}/${player.pointsToNextLevel}"
        maxHealthLabel.text = "Max Health: ${player.maxHealth}"
        healthLabel.text = "Health: ${player.health}"
        attackLabel.text = "Attack: ${player.attack}"
        defenceLabel.text = "Defense: ${player.defense}"
    }
}

