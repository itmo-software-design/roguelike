package engine.state

import engine.behaviour.Behaviour
import engine.behaviour.FearfulBehaviour

/**
 * Паническое состояние [vo.Character]
 *
 * @author gkashin
 */
class PanicState(defaultBehaviour: Behaviour) : DefaultState(defaultBehaviour) {
    override fun getBehaviour(): Behaviour = FearfulBehaviour(defaultBehaviour)
}
