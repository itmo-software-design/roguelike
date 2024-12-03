package engine

import vo.tile.*

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
object GameSession {

    lateinit var playerName: String

    var tileMap: Array<Array<Tile>> = arrayOf(
        arrayOf(WallTile(), WallTile(), WallTile(), WallTile(), WallTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), GroundTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), GrassTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), WaterTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), WaterTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), WaterTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), WaterTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), WaterTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), WaterTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), WaterTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), GrassTile(), GroundTile(), GroundTile(), GrassTile(), WallTile()),
        arrayOf(WallTile(), WallTile(), WallTile(), WallTile(), WallTile(), WallTile()),
    )


}
