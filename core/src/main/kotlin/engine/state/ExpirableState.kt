package engine.state

import engine.behaviour.Behaviour
import engine.behaviour.PassiveBehaviour

/**
 * Состояние, ограниченное по времени
 *
 * @author MikhailShad
 */
abstract class ExpirableState(
    duration: Int,
    defaultBehaviour: Behaviour = PassiveBehaviour()
) : DefaultState(defaultBehaviour) {
    private var remainingTurns = duration

    val isExpired get() = remainingTurns <= 0

    fun tick() {
        remainingTurns -= 1
    }
}
