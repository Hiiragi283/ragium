package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTComponentEntityBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.text.addFluidTooltip
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.state.BlockState

class HTDrumBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTComponentEntityBlock(type, properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any()

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(RagiumCapabilities.FLUID.getCapabilityStacks(stack), tooltips::add, flag)
    }
}
