package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import net.minecraft.block.Block
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTTieredBlockPattern private constructor(val blockGetter: (HTMachineTier) -> Block) : HTMultiblockPattern {
    companion object {
        @JvmStatic
        fun ofBlock(blockGetter: (HTMachineTier) -> Block): HTTieredBlockPattern = HTTieredBlockPattern(blockGetter)

        @JvmStatic
        fun ofContent(blockGetter: (HTMachineTier) -> HTContent<Block>): HTTieredBlockPattern = ofBlock { blockGetter(it).get() }
    }

    override val text: MutableText = blockGetter(HTMachineTier.PRIMITIVE).name

    override fun test(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean {
        val block: Block = HTMachineTier.SIDED_LOOKUP.find(world, provider.multiblockManager.pos, null)?.let(blockGetter) ?: return false
        return world.getBlockState(pos).isOf(block)
    }
}
