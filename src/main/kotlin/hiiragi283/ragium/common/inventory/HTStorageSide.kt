package hiiragi283.ragium.common.inventory

import net.minecraft.util.math.Direction

@ConsistentCopyVisibility
data class HTStorageSide private constructor(val directions: Collection<Direction>) {
    companion object {
        @JvmField
        val NONE = HTStorageSide()

        @JvmField
        val ANY = HTStorageSide(Direction.entries)

        @JvmField
        val UP = HTStorageSide(Direction.UP)

        @JvmField
        val DOWN = HTStorageSide(Direction.DOWN)

        @JvmField
        val SIDE = HTStorageSide(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
    }

    constructor(vararg directions: Direction) : this(directions.toSortedSet())
}
