package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.storage.amount.HTAmountSlot
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.Text
import hiiragi283.core.support.capability.HTEnergyCapabilities
import hiiragi283.core.util.HTStorageHelper
import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTBatteryBlockItem(block: HTBatteryBlock, properties: Properties) : HTStorageBlockItem<HTBatteryBlock>(block, properties) {
    /**
     * @see mekanism.common.item.block.ItemBlockEnergyCube.addStats
     */
    override fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {
        val isCreative: Boolean = isCreative(stack)
        val view: HTAmountSlot = HTEnergyCapabilities.getHandler(stack) ?: return
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

    override fun isBarVisible(stack: ItemStack): Boolean = stack.count == 1 && !isCreative(stack)

    override fun getBarWidth(stack: ItemStack): Int = HTStorageHelper.getEnergyBarWidth(stack)

    override fun getBarColor(stack: ItemStack): Int = RagiumConfig.COMMON.energyBarColor.get()
}
