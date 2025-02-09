package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTSimpleMultiblockComponent(val block: Block) : HTMultiblockComponent {
    override fun getType(): HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.SIMPLE.get()

    override fun getBlockName(controller: HTControllerDefinition): Component = ItemStack(block).displayName

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean = controller.level.getBlockState(pos).`is`(block)

    override fun getPlacementState(controller: HTControllerDefinition): BlockState? = block.defaultBlockState()
}
