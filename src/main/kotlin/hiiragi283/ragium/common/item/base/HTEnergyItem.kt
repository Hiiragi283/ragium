package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.text.HTTextUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

/**
 * @see mekanism.common.item.ItemEnergized
 */
open class HTEnergyItem(properties: Properties) : Item(properties) {
    protected fun getBattery(stack: ItemStack): HTEnergyBattery? = HTEnergyCapabilities.getBattery(stack)

    override fun isBarVisible(stack: ItemStack): Boolean = stack.count == 1

    override fun getBarWidth(stack: ItemStack): Int {
        val battery: HTEnergyBattery = getBattery(stack) ?: return 0
        return (13f * battery.getStoredLevelAsFloat()).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = 0xff003f

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val battery: HTEnergyBattery = getBattery(stack) ?: return
        HTTextUtil.addEnergyTooltip(battery, tooltips::add)
    }
}
