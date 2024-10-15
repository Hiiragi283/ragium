package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.util.math.Direction

fun interface HTMultiblockBuilder {
    /**
     * @see [reborncore.common.blockentity.MultiblockWriter.rotate]
     */
    private fun rotate(): HTMultiblockBuilder = HTMultiblockBuilder { x: Int, y: Int, z: Int, component: HTMultiblockComponent ->
        add(-z, y, x, component)
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
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder = apply {
        for (y: Int in yRange) {
            add(x, y, z, component)
        }
    }

    fun addLayer(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                add(x, y, z, component)
            }
        }
    }

    fun addHollow(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                if (x == xRange.first || x == xRange.last || z == zRange.first || z == zRange.last) {
                    add(x, y, z, component)
                }
            }
        }
    }

    fun addCross4(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                if ((x != xRange.first && x != xRange.last) || (z != zRange.first && z != zRange.last)) {
                    add(x, y, z, component)
                }
            }
        }
    }

    fun add(
        x: Int,
        y: Int,
        z: Int,
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder
}
