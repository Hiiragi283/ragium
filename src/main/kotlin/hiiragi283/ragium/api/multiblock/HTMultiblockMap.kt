package hiiragi283.ragium.api.multiblock

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

/**
 * 座標と紐づけたマルチブロックの各要素を持つマップ
 */
sealed class HTMultiblockMap(val map: Map<BlockPos, HTMultiblockComponent>) : Map<BlockPos, HTMultiblockComponent> by map {
    companion object {
        /**
         * @see [reborncore.common.blockentity.MultiblockWriter.rotate]
         */
        @JvmStatic
        private fun rotate(pos: BlockPos): BlockPos = BlockPos(-pos.z, pos.y, pos.x)

        @JvmStatic
        fun rotate(front: Direction, pos: BlockPos): BlockPos = when (front) {
            Direction.SOUTH -> pos.let(::rotate).let(::rotate)
            Direction.WEST -> pos.let(::rotate).let(::rotate).let(::rotate)
            Direction.EAST -> pos.let(::rotate)
            else -> pos
        }

        @JvmStatic
        fun builder(): Builder = Builder()
    }

    //    Relative    //

    /**
     * 相対座標を用いた実装
     */
    class Relative internal constructor(map: Map<BlockPos, HTMultiblockComponent>) : HTMultiblockMap(map) {
        fun convertAbsolute(controller: HTControllerDefinition): Absolute = convertAbsolute(controller.pos, controller.front)

        fun convertAbsolute(origin: BlockPos, front: Direction): Absolute =
            Absolute(map.mapKeys { (pos: BlockPos, _: HTMultiblockComponent) -> origin.add(rotate(front, pos)) })
    }

    //    Absolute    //

    /**
     * 絶対座標を用いた実装
     */
    class Absolute internal constructor(map: Map<BlockPos, HTMultiblockComponent>) : HTMultiblockMap(map)

    //    Builder    //

    class Builder {
        private val map: MutableMap<BlockPos, HTMultiblockComponent> = mutableMapOf()

        fun addPillar(
            x: Int,
            yRange: IntRange,
            z: Int,
            component: HTMultiblockComponent,
        ): Builder = apply {
            for (y: Int in yRange) {
                add(x, y, z, component)
            }
        }

        fun addLayer(
            xRange: IntRange,
            y: Int,
            zRange: IntRange,
            component: HTMultiblockComponent,
        ): Builder = apply {
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
        ): Builder = apply {
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
        ): Builder = apply {
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
        ): Builder = add(BlockPos(x, y, z), component)

        fun add(pos: BlockPos, component: HTMultiblockComponent): Builder = apply {
            if (pos == BlockPos.ZERO) return@apply
            map[pos] = component
        }

        fun build(): Relative = Relative(map)
    }
}
