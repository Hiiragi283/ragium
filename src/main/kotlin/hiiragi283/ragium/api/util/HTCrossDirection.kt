package hiiragi283.ragium.api.util

import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Direction

enum class HTCrossDirection(val first: Direction, val second: Direction, val third: Direction) : StringIdentifiable {
    // UP-A-B
    UP_NORTH_EAST(Direction.UP, Direction.NORTH, Direction.EAST),
    UP_EAST_SOUTH(Direction.UP, Direction.EAST, Direction.SOUTH),
    UP_SOUTH_WEST(Direction.UP, Direction.SOUTH, Direction.WEST),
    UP_WEST_NORTH(Direction.UP, Direction.WEST, Direction.NORTH),

    // DOWN-A-B
    DOWN_NORTH_EAST(Direction.DOWN, Direction.NORTH, Direction.EAST),
    DOWN_EAST_SOUTH(Direction.DOWN, Direction.EAST, Direction.SOUTH),
    DOWN_SOUTH_WEST(Direction.DOWN, Direction.SOUTH, Direction.WEST),
    DOWN_WEST_NORTH(Direction.DOWN, Direction.WEST, Direction.NORTH),
    ;

    val directions: List<Direction> = listOf(first, second, third)

    override fun asString(): String = name.lowercase()
}
