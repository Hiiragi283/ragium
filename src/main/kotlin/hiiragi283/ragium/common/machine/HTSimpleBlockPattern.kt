package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSimpleBlockPattern(val block: Block) : HTMultiblockPattern {
    constructor(content: HTBlockContent) : this(content.get())

    override val text: Text = ItemStack(block).name

    override fun checkState(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean = world.getBlockState(pos).isOf(block)

    override fun getPlacementState(world: World, pos: BlockPos, provider: HTMultiblockProvider): BlockState = block.defaultState
}
