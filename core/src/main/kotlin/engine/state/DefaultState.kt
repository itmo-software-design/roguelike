package engine.state

import engine.behaviour.Behaviour
import engine.behaviour.PassiveBehaviour

/**
 * Абстрактный предок для состояния [vo.Character]
 *
 * @author MikhailShad
 */
abstract class DefaultState(protected val defaultBehaviour: Behaviour = PassiveBehaviour()) : State
