package engine.state

import engine.behaviour.Behaviour

/**
 * Нормальное состояние [vo.Character]
 *
 * @author gkashin
 */
class NormalState(defaultBehaviour: Behaviour) : DefaultState(defaultBehaviour)
