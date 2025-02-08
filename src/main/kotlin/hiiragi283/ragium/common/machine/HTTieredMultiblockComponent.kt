package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.RagiumReferences
import hiiragi283.ragium.api.extension.getItemData
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Function
import java.util.function.Supplier

class HTTieredMultiblockComponent(val getter: Function<HTMachineTier, out Supplier<out Block>>) : HTMultiblockComponent {
    override fun getType(): HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.TIER.get()

    fun getBlock(controller: HTControllerDefinition): Block =
        (controller.state.getItemData(RagiumReferences.DataMapTypes.MACHINE_TIER) ?: HTMachineTier.BASIC)
            .let(getter::apply)
            .get()

    override fun getBlockName(controller: HTControllerDefinition): Component = ItemStack(getBlock(controller)).displayName

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean =
        controller.level.getBlockState(pos).`is`(getBlock(controller))

    override fun getPlacementState(controller: HTControllerDefinition): BlockState? = getBlock(controller).defaultBlockState()
}
