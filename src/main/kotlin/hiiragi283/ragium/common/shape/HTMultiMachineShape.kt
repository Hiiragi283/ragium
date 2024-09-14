package hiiragi283.ragium.common.shape

import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiPredicate

class HTMultiMachineShape private constructor(private val posMap: Map<BlockPos, HTBlockPredicate>) :
    BiPredicate<World, BlockPos> {

    companion object {
        // https://github.com/AztechMC/Modern-Industrialization/blob/1.20.x/src/main/java/aztech/modern_industrialization/machines/multiblocks/ShapeMatcher.java#L63
        @JvmStatic
        fun toAbsolutePos(originPos: BlockPos, originDirection: Direction?, relativePos: BlockPos): BlockPos =
            when (originDirection) {
                Direction.SOUTH -> BlockPos(-relativePos.x, relativePos.y, -relativePos.z)
                Direction.WEST -> BlockPos(relativePos.z, relativePos.y, -relativePos.x)
                Direction.EAST -> BlockPos(-relativePos.z, relativePos.y, relativePos.x)
                else -> relativePos
            }.add(originPos)
    }

    fun getAbsolutePattern(
        originPos: BlockPos,
        originDirection: Direction? = null,
    ): Map<BlockPos, HTBlockPredicate> =
        posMap.map { (pos: BlockPos, predicate: HTBlockPredicate) ->
            toAbsolutePos(
                originPos,
                originDirection,
                pos,
            ) to predicate
        }.toMap()

    override fun test(world: World, originPos: BlockPos): Boolean = test(world, originPos, null)

    fun test(world: World, originPos: BlockPos, player: PlayerEntity?): Boolean {
        if (world.isClient || world !is ServerWorld) return false
        val state: BlockState = world.getBlockState(originPos)
        val direction: Direction? = when (state.contains(Properties.HORIZONTAL_FACING)) {
            true -> state.get(Properties.HORIZONTAL_FACING)
            false -> null
        }
        val absolutePattern: Map<BlockPos, HTBlockPredicate> = getAbsolutePattern(originPos, direction)
        for ((pos: BlockPos, predicate: HTBlockPredicate) in absolutePattern) {
            if (!predicate.test(world, pos)) {
                player?.sendMessage(
                    Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_ERROR, predicate, pos),
                    false
                )
                return false
            }
        }
        player?.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), true)
        return true
    }

    //    Builder    //

    class Builder {
        private val posMap: MutableMap<BlockPos, HTBlockPredicate> = mutableMapOf()

        fun addPillar(
            x: Int,
            yRange: IntRange,
            z: Int,
            predicate: HTBlockPredicate,
        ): Builder = apply {
            for (y: Int in yRange) {
                add(x, y, z, predicate)
            }
        }

        fun addLayer(
            xRange: IntRange,
            y: Int,
            zRange: IntRange,
            predicate: HTBlockPredicate,
        ): Builder = apply {
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
        ): Builder = apply {
            for (x: Int in xRange) {
                for (z: Int in zRange) {
                    if (x == xRange.first || x == xRange.last || z == zRange.first || z == zRange.last) {
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
        ): Builder = add(BlockPos(x, y, z), predicate)

        private fun add(pos: BlockPos, predicate: HTBlockPredicate): Builder = apply {
            posMap[pos] = predicate
        }

        fun remove(x: Int, y: Int, z: Int): Builder = remove(BlockPos(x, y, z))

        private fun remove(pos: BlockPos): Builder = apply {
            posMap.remove(pos)
        }

        fun build(): HTMultiMachineShape {
            remove(0, 0, 0)
            return HTMultiMachineShape(posMap)
        }
    }

}