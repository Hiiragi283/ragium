package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.HTAmountSlot
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import net.minecraft.ChatFormatting
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
        flag: TooltipFlag,
    ) {
        val isCreative: Boolean = HTUpgradeHelper.isCreative(stack)
        val view: HTAmountSlot.IntSized = HTEnergyCapabilities.getBattery(stack) ?: return
        // Energy Amount
        val amount: Int = view.getAmount()
        when {
            amount <= 0 -> RagiumTranslation.EMPTY.translateColored(ChatFormatting.DARK_RED)
            isCreative -> RagiumTranslation.STORED_FE.translateColored(
                ChatFormatting.GRAY,
                RagiumTranslation.INFINITE,
            )
            else -> RagiumTranslation.STORED_FE.translateColored(
                ChatFormatting.GRAY,
                amount,
            )
        }.let(tooltips::add)
        // Energy Capacity
        when (isCreative) {
            true -> RagiumTranslation.CAPACITY.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                RagiumTranslation.INFINITE,
            )
            false -> RagiumTranslation.CAPACITY_FE.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                view.getCapacity(),
            )
        }.let(tooltips::add)
    }
}
