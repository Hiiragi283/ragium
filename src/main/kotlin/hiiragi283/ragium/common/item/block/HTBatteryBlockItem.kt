package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTBatteryBlockItem(block: HTBatteryBlock, properties: Properties) : HTStorageBlockItem<HTBatteryBlock>(block, properties) {
    /**
     * @see mekanism.common.item.block.ItemBlockEnergyCube.addStats
     */
    override fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag
    ) {
        super.addStats(stack, context, tooltips, flag)
    }
}
