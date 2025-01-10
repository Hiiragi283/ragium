package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class HTSimpleMultiblockComponent(val block: Block) : HTMultiblockComponent {
    constructor(content: HTBlockContent) : this(content.get())

    override val type: HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.SIMPLE

    override fun getBlockName(controller: HTControllerDefinition): Text = ItemStack(block).name

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean = controller.world.getBlockState(pos).isOf(block)

    override fun getPlacementState(controller: HTControllerDefinition): BlockState? = block.defaultState
}
