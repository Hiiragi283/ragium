package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.block.BlockState
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Represent a predicate for multiblock component
 */
interface HTMultiblockPattern {
    val text: MutableText

    fun checkState(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean

    fun getPlacementState(world: World, pos: BlockPos, provider: HTMultiblockProvider): BlockState?
}
