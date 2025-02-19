package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block

class HTBlockItem(block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        super.appendHoverText(stack, context, tooltip, flag)
        val consumer: (Component) -> Unit = tooltip::add
        // Fluid Content
        addFluidTooltip(stack, consumer)
        // Spawner Info
        stack.get(RagiumComponentTypes.SPAWNER_CONTENT)?.addToTooltip(context, consumer, flag)
    }
}
