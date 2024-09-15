package hiiragi283.ragium.common.inventory

import net.minecraft.util.math.Direction

enum class HTStorageSides(val directions: Collection<Direction>) {
    UP(setOf(Direction.UP)),
    SIDE(setOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)),
    DOWN(setOf(Direction.DOWN)),
    ANY(Direction.entries),
    NONE(setOf()),
}
