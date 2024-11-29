package hiiragi283.ragium.api.storage

import net.minecraft.util.math.Direction

enum class HTStorageSide(val directions: Collection<Direction>) {
    NONE(),
    ANY(Direction.entries),
    ;

    constructor(vararg directions: Direction) : this(directions.toSortedSet())
}
