package hiiragi283.ragium.common.multiblock

import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class HTSimpleMultiblockComponent(val block: Supplier<out Block>) : HTMultiblockComponent {
    override fun getBlockName(definition: HTControllerDefinition): Component = block.get().let(::ItemStack).displayName

    override fun checkState(definition: HTControllerDefinition, pos: BlockPos): Boolean =
        definition.level.getBlockState(pos).`is`(block.get())

    override fun getPlacementState(definition: HTControllerDefinition): BlockState? = block.get().defaultBlockState()
}
