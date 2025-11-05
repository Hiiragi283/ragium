package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.text.addEnergyTooltip
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

/**
 * @see de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy
 */
abstract class HTEnergyItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getStorage(stack: ItemStack): HTEnergyBattery? = HTEnergyCapabilities.getBattery(stack)

        @JvmStatic
        fun hasStorage(stack: ItemStack): Boolean = getStorage(stack) != null

        @JvmStatic
        fun extractEnergy(stack: ItemStack, amount: Int, action: HTStorageAction): Int =
            getStorage(stack)?.extract(amount, action, HTStorageAccess.INTERNAL) ?: 0
    }

    //    Item    //

    override fun isBarVisible(stack: ItemStack): Boolean = hasStorage(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val battery: HTEnergyBattery = getStorage(stack) ?: return 0
        return (13f * battery.getStoredLevelAsFloat()).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = 0xff003f

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        val battery: HTEnergyBattery = getStorage(stack) ?: return
        addEnergyTooltip(battery, tooltipComponents::add)
    }

    override fun isEnchantable(stack: ItemStack): Boolean = stack.maxStackSize == 1 && hasStorage(stack)

    //    User    //

    abstract class User(properties: Properties) : HTEnergyItem(properties) {
        protected abstract val energyUsage: Int
    }
}
