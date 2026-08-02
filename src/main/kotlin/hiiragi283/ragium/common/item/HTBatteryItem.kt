package hiiragi283.ragium.common.item

import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.api.text.Text
import hiiragi283.core.support.capability.HTEnergyCapabilities
import hiiragi283.core.util.HTStorageHelper
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

/**
 * @see mekanism.common.item.ItemEnergized
 */
open class HTBatteryItem(properties: Properties) : Item(properties) {
    override fun isBarVisible(stack: ItemStack): Boolean = stack.count == 1

    override fun getBarWidth(stack: ItemStack): Int = HTStorageHelper.getEnergyBarWidth(stack)

    override fun getBarColor(stack: ItemStack): Int = RagiumConfig.SERVER.energyBarColor.get()

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        tooltipFlag: TooltipFlag,
    ) {
        val handler: HTEnergyHandler = HTEnergyCapabilities.getHandler(stack) ?: return
        HTStorageHelper.addEnergyTooltip(handler, tooltips::add, false)
    }

    override fun shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean): Boolean = slotChanged || oldStack.item != newStack.item

    override fun shouldCauseBlockBreakReset(oldStack: ItemStack, newStack: ItemStack): Boolean = oldStack.item != newStack.item
}
