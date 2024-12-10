package engine.behaviour

import engine.action.MoveAction
import vo.DungeonLevel
import vo.Mob
import vo.Player
import vo.Position

/**
 * Базовое поведение моба
 *
 * @since MikhailShad
 * @since 0.0.1
 */
open class BasicBehaviour : Behaviour {
    private val path: ArrayDeque<Position> = ArrayDeque()

    /**
     * Бродит по уровню
     */
    override fun act(mob: Mob, dungeonLevel: DungeonLevel, player: Player) {
        if (path.isEmpty()) {
            rebuildPath(mob, dungeonLevel)
        }

        val nextDestination = path.removeFirst()
        if (!MoveAction.perform(mob, nextDestination, dungeonLevel)) {
            // Если подвигаться не получилось, попробуем перестроить маршрут
            rebuildPath(mob, dungeonLevel)
        }
    }

    private fun rebuildPath(mob: Mob, dungeonLevel: DungeonLevel) {
        path.clear()
        val sortedRooms = dungeonLevel.rooms.maxBy {
            it.center.euclideanDistanceTo(mob.position)
        }
        path.addAll(findPath(mob.position, sortedRooms.center, dungeonLevel))
    }

    companion object {
        /**
         * Узел в пути
         */
        private data class Node(
            val position: Position,
            /**
             * Стоимость от начальной точки до текущего узла
             */
            val g: Int,
            /**
             * Эвристическая оценка
             */
            val h: Int,
            val parent: Node? = null
        ) {
            /**
             * Общая стоимость пути
             */
            val f: Int get() = g + h
        }

        /**
         * Находит путь с использованием алгоритма A*.
         *
         * @param start начальная позиция
         * @param end конечная позиция
         * @param level карта уровня, где можно получить информацию о препятствиях
         * @return список позиций, представляющих путь от start до end
         */
        fun findPath(start: Position, end: Position, level: DungeonLevel): List<Position> {
            val openList = mutableListOf<Node>() // Список узлов для проверки
            val closedList = mutableSetOf<Position>() // Список исследованных узлов

            // Добавляем начальный узел в открытый список
            openList.add(Node(start, 0, start.manhattanDistanceTo(end)))

            while (openList.isNotEmpty()) {
                // Извлекаем узел с наименьшей f стоимости
                val currentNode = openList.minByOrNull { it.f } ?: continue

                // Если достигнут конечный узел, восстанавливаем путь
                if (currentNode.position == end) {
                    return reconstructPath(currentNode)
                }

                // Перемещаем текущий узел в закрытый список
                openList.remove(currentNode)
                closedList.add(currentNode.position)

                // Проверяем соседние узлы
                for (neighbor in getNeighbors(currentNode.position)) {
                    // Пропускаем если соседний узел уже исследован или является препятствием
                    if (closedList.contains(neighbor) || !isWalkable(neighbor, level)) {
                        continue
                    }

                    // Вычисляем стоимость пути до соседнего узла
                    val tentativeG = currentNode.g + 1
                    val existingNode = openList.find { it.position == neighbor }

                    // Если соседний узел не в открытом списке или мы нашли более короткий путь
                    if (existingNode == null || tentativeG < existingNode.g) {
                        val h = neighbor.manhattanDistanceTo(end)
                        val newNode = Node(neighbor, tentativeG, h, currentNode)
                        openList.add(newNode)
                    }
                }
            }

            // Возвращаем пустой список, если путь не найден
            return emptyList()
        }

        /**
         * Восстанавливает путь от конечного узла
         */
        private fun reconstructPath(node: Node): List<Position> {
            val path = mutableListOf<Position>()
            var currentNode: Node? = node
            while (currentNode != null) {
                path.add(currentNode.position)
                currentNode = currentNode.parent
            }
            return path.reversed()
                .drop(1) // исключаем стартовую ноду
        }

        /**
         * Получаем соседние клетки
         */
        private fun getNeighbors(position: Position): List<Position> {
            return listOf(
                Position(position.x - 1, position.y),
                Position(position.x + 1, position.y),
                Position(position.x, position.y - 1),
                Position(position.x, position.y + 1)
            )
        }

        /**
         * Проверка, можно ли пройти по данному месту (не является ли оно стеной или другим препятствием)
         */
        private fun isWalkable(position: Position, level: DungeonLevel): Boolean {
            val tile = level.getTileAt(position)
            return !tile.type.blocked
        }
    }
}
