package hiiragi283.ragium.common.machine

import net.minecraft.util.math.Direction

fun interface HTMultiblockBuilder {
    /**
     * @see [reborncore.common.blockentity.MultiblockWriter.rotate]
     */
    private fun rotate(): HTMultiblockBuilder = HTMultiblockBuilder { x: Int, y: Int, z: Int, predicate: HTBlockPredicate ->
        add(-z, y, x, predicate)
    }

    fun rotate(direction: Direction?): HTMultiblockBuilder = when (direction) {
        Direction.SOUTH -> rotate().rotate()
        Direction.WEST -> rotate().rotate().rotate()
        Direction.EAST -> rotate()
        else -> this
    }

    fun addPillar(
        x: Int,
        yRange: IntRange,
        z: Int,
        predicate: HTBlockPredicate,
    ): HTMultiblockBuilder = apply {
        for (y: Int in yRange) {
            add(x, y, z, predicate)
        }
    }

    fun addLayer(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        predicate: HTBlockPredicate,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                add(x, y, z, predicate)
            }
        }
    }

    fun addHollow(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        predicate: HTBlockPredicate,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                if (x == xRange.first || x == xRange.last || z == zRange.first || z == zRange.last) {
                    add(x, y, z, predicate)
                }
            }
        }
    }

    fun addCross4(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        predicate: HTBlockPredicate,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                if ((x != xRange.first && x != xRange.last) || (z != zRange.first && z != zRange.last)) {
                    add(x, y, z, predicate)
                }
            }
        }
    }

    fun add(
        x: Int,
        y: Int,
        z: Int,
        predicate: HTBlockPredicate,
    ): HTMultiblockBuilder
}
