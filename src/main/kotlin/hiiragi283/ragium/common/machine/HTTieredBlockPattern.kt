package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTTieredBlockPattern private constructor(val blockGetter: (HTMachineTier) -> Block) : HTMultiblockPattern {
    companion object {
        @JvmStatic
        fun ofBlock(blockGetter: (HTMachineTier) -> Block): HTTieredBlockPattern = HTTieredBlockPattern(blockGetter)

        @JvmStatic
        fun ofContent(blockGetter: (HTMachineTier) -> HTBlockContent): HTTieredBlockPattern = ofBlock { blockGetter(it).get() }
    }

    override val text: Text = ItemStack(blockGetter(HTMachineTier.PRIMITIVE)).name

    fun getBlock(world: World, provider: HTMultiblockProvider): Block? =
        HTMachineTier.SIDED_LOOKUP.find(world, provider.multiblockManager.pos, null)?.let(blockGetter)

    override fun checkState(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean {
        val block: Block = getBlock(world, provider) ?: return false
        return world.getBlockState(pos).isOf(block)
    }

    override fun getPlacementState(world: World, pos: BlockPos, provider: HTMultiblockProvider): BlockState? =
        getBlock(world, provider)?.defaultState
}
