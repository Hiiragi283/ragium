package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class HTTieredMultiblockComponent(val blockGetter: (HTMachineTier) -> HTBlockContent) : HTMultiblockComponent {
    override val text: Text = ItemStack(blockGetter(HTMachineTier.PRIMITIVE)).name

    fun getBlock(controller: HTControllerDefinition): Block? = controller.find(HTMachineTier.SIDED_LOOKUP)?.let(blockGetter)?.get()

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean {
        val block: Block = getBlock(controller) ?: return false
        return controller.world.getBlockState(pos).isOf(block)
    }

    override fun getPlacementState(controller: HTControllerDefinition, pos: BlockPos): BlockState? = getBlock(controller)?.defaultState
}
