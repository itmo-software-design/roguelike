package engine.action

import vo.Character
import vo.DungeonLevel

/**
 * Действие, выполняемое актором
 *
 * @param T тип цели
 * @param R тип результата выполнения действия
 *
 * @author MikhailShad
 * @since 0.0.1
 */
interface Action<T, R> {
    /**
     * Логика выполнения действия
     *
     * @param actor кто выполняет действие
     * @param target над какой целью
     * @param dungeonLevel на каком уровне
     * @return результат выполнения действия
     */
    fun perform(actor: Character, target: T, dungeonLevel: DungeonLevel): R
}
