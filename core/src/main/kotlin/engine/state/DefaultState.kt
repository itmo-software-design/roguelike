package engine.state

import engine.behaviour.Behaviour

/**
 * Абстрактный предок для состояния [vo.Character]
 *
 * @author MikhailShad
 */
abstract class DefaultState(protected val defaultBehaviour: Behaviour) : State {
    override fun getBehaviour(): Behaviour = defaultBehaviour
}
