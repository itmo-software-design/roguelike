package engine.behaviour

/**
 * Декоратор для поведения моба
 *
 * @author MikhailShad
 * @since 0.0.1
 */
abstract class BehaviourDecorator(protected val parentBehaviour: Behaviour) : Behaviour
