package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.util.math.Direction

fun interface HTMultiblockBuilder {
    /**
     * @see [reborncore.common.blockentity.MultiblockWriter.rotate]
     */
    private fun rotate(): HTMultiblockBuilder = HTMultiblockBuilder { x: Int, y: Int, z: Int, pattern: HTMultiblockPattern ->
        add(-z, y, x, pattern)
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
        pattern: HTMultiblockPattern,
    ): HTMultiblockBuilder = apply {
        for (y: Int in yRange) {
            add(x, y, z, pattern)
        }
    }

    fun addLayer(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        pattern: HTMultiblockPattern,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                add(x, y, z, pattern)
            }
        }
    }

    fun addHollow(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        pattern: HTMultiblockPattern,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                if (x == xRange.first || x == xRange.last || z == zRange.first || z == zRange.last) {
                    add(x, y, z, pattern)
                }
            }
        }
    }

    fun addCross4(
        xRange: IntRange,
        y: Int,
        zRange: IntRange,
        pattern: HTMultiblockPattern,
    ): HTMultiblockBuilder = apply {
        for (x: Int in xRange) {
            for (z: Int in zRange) {
                if ((x != xRange.first && x != xRange.last) || (z != zRange.first && z != zRange.last)) {
                    add(x, y, z, pattern)
                }
            }
        }
    }

    fun add(data: HTMultiblockPattern.Data) {
        add(data.x, data.y, data.z, data.pattern)
    }

    fun add(
        x: Int,
        y: Int,
        z: Int,
        pattern: HTMultiblockPattern,
    )

    fun interface Consumer {
        /**
         * [builder]にマルチブロックの構造を提供します。
         */
        fun buildMultiblock(builder: HTMultiblockBuilder)
    }
}
