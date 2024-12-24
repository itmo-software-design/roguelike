package engine.state

import engine.behaviour.Behaviour

/**
 * Состояние [vo.Character]
 *
 * @author gkashin
 */
interface State {
    /**
     * Возвращает поведение моба согласно его состоянию
     */
    fun getBehaviour(): Behaviour
}
