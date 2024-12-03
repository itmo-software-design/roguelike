package engine

import vo.tile.*

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
object GameSession {

    lateinit var playerName: String

    var tileMap: Array<Array<Tile>> = Array(32) { Array(32) { GrassTile() } }

    init {
        for ((y, tiles) in tileMap.withIndex()) {
            for ((x, _) in tiles.withIndex()) {
                when {
                    y == 0 || x == 0 || y == tileMap.size - 1 || x == tiles.size - 1 -> {
                        tileMap[y][x] = WallTile()
                    }
                    y % 4 == 0 && x % 4 == 0 -> {
                        tileMap[y][x] = WaterTile()
                    }
                    y == tileMap.size / 2 || x == tileMap.size / 2 -> {
                        tileMap[y][x] = GroundTile()
                    }
                }
            }
        }
    }

}
