package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.capability.HTFluidCapabilities
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.storage.HTTankBlock
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTTankBlockItem(block: HTTankBlock, properties: Properties) :
    HTStorageBlockItem<HTTankBlock>(block, properties),
    HTSubCreativeTabContents {
    /**
     * @see mekanism.common.item.block.machine.ItemBlockFluidTank.addStats
     */
    override fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val isCreative: Boolean = HTUpgradeHelper.isCreative(stack)
        val view: HTFluidView = HTFluidCapabilities.getFluidView(stack, 0) ?: return
        // Fluid Name
        val resource: HTFluidResourceType? = view.getResource()
        when {
            resource == null -> HTCommonTranslation.EMPTY.translateColored(HTDefaultColor.RED)
            isCreative -> HTCommonTranslation.STORED.translateColored(
                HTDefaultColor.PURPLE,
                resource,
                HTDefaultColor.GRAY,
                HTCommonTranslation.INFINITE,
            )
            else -> HTCommonTranslation.STORED_MB.translateColored(
                HTDefaultColor.PURPLE,
                resource,
                HTDefaultColor.GRAY,
                view.getAmount(),
            )
        }.let(tooltips::add)
        // Tank Capacity
        when (isCreative) {
            true -> HTCommonTranslation.CAPACITY.translateColored(
                HTDefaultColor.BLUE,
                HTDefaultColor.GRAY,
                HTCommonTranslation.INFINITE,
            )
            false -> HTCommonTranslation.CAPACITY_MB.translateColored(
                HTDefaultColor.BLUE,
                HTDefaultColor.GRAY,
                view.getCapacity(),
            )
        }.let(tooltips::add)
    }
}
