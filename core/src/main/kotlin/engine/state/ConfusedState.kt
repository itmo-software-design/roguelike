package engine.state

import engine.behaviour.Behaviour
import engine.behaviour.ConfusedBehaviour

/**
 * Сконфуженное состояние
 *
 * @author MikhailShad
 */
class ConfusedState(duration: Int) : ExpirableState(duration) {
    override fun getBehaviour(): Behaviour = ConfusedBehaviour()
}
