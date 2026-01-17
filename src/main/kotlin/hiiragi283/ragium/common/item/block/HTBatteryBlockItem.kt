package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.storage.amount.HTAmountSlot
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
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
        flag: TooltipFlag,
    ) {
        val isCreative: Boolean = HTUpgradeHelper.isCreative(stack)
        val view: HTAmountSlot = HTEnergyCapabilities.getBattery(stack) ?: return
        // Energy Amount
        val amount: Int = view.getAmount()
        when {
            isCreative -> HTCommonTranslation.STORED_FE.translateColored(
                HTDefaultColor.GRAY,
                HTCommonTranslation.INFINITE,
            )
            amount <= 0 -> HTCommonTranslation.EMPTY.translateColored(HTDefaultColor.RED)
            else -> HTCommonTranslation.STORED_FE.translateColored(
                HTDefaultColor.GRAY,
                amount,
            )
        }.let(tooltips::add)
        // Energy Capacity
        when (isCreative) {
            true -> HTCommonTranslation.CAPACITY.translateColored(
                HTDefaultColor.BLUE,
                HTDefaultColor.GRAY,
                HTCommonTranslation.INFINITE,
            )
            false -> HTCommonTranslation.CAPACITY_FE.translateColored(
                HTDefaultColor.BLUE,
                HTDefaultColor.GRAY,
                view.getCapacity(),
            )
        }.let(tooltips::add)
    }
}
