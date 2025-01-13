package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class HTSimpleMultiblockComponent(val block: Supplier<Block>) : HTMultiblockComponent {
    override val type: HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.SIMPLE.get()

    override fun getBlockName(controller: HTControllerDefinition): Component = ItemStack(block.get()).displayName

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean =
        controller.level.getBlockState(pos).`is`(block.get())

    override fun getPlacementState(controller: HTControllerDefinition): BlockState? = block.get().defaultBlockState()
}
