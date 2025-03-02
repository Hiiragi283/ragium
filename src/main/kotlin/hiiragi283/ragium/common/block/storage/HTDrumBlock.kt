package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.util.HTDrumVariant
import hiiragi283.ragium.common.block.HTEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.state.BlockState

class HTDrumBlock(private val variant: HTDrumVariant, properties: Properties) : HTEntityBlock(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(stack, tooltip::add)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTDrumBlockEntity = HTDrumBlockEntity(pos, state)
}
