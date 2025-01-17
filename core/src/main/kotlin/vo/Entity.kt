package vo

/**
 * Абстрактный предок для всех сущностей, которых можно встретить в игре
 *
 * @author MikhailShad
 * @since 0.0.1
 */
abstract class Entity(
    /**
     * Позиция на уровне
     */
    var position: Position
) {
    /**
     * Символ, которым отображается сущность
     */
    abstract var symbol: Char
}
