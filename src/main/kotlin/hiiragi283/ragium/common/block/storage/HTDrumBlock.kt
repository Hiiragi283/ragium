package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.fluidAmountText
import hiiragi283.ragium.api.extension.fluidCapacityText
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.common.block.HTEntityBlock
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTDrumBlock(properties: Properties) : HTEntityBlock(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        val content: SimpleFluidContent = stack.get(RagiumComponentTypes.FLUID_CONTENT) ?: return
        // Title
        tooltipComponents.add(content.fluidType.description)
        // Amount
        tooltipComponents.add(fluidAmountText(content.amount))
        // Capacity
        val enchLevel: Int = stack.getLevel(context.registries(), RagiumEnchantments.CAPACITY)
        tooltipComponents.add(fluidCapacityText(RagiumAPI.DEFAULT_TANK_CAPACITY * (enchLevel + 1)))
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTDrumBlockEntity = HTDrumBlockEntity(pos, state)
}
