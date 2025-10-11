package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.network.addEnergyTooltip
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.common.util.HTItemHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

/**
 * @see [de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy]
 */
abstract class HTEnergyItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getHandler(stack: ItemStack): HTEnergyHandler? = HTMultiCapability.ENERGY.getCapability(stack) as? HTEnergyHandler

        @JvmStatic
        fun hasHandler(stack: ItemStack): Boolean = getHandler(stack) != null

        @JvmStatic
        fun getBattery(stack: ItemStack): HTEnergyBattery? {
            val handler: HTEnergyHandler = getHandler(stack) ?: return null
            return handler.getEnergyHandler(handler.getEnergySideFor())
        }

        @JvmStatic
        fun extractEnergy(stack: ItemStack, amount: Int, action: HTStorageAction): Int =
            getBattery(stack)?.extractEnergy(amount, action, HTStorageAccess.INTERNAL) ?: 0

        @JvmStatic
        fun canConsumeEnergy(stack: ItemStack, amount: Int): Boolean {
            val battery: HTEnergyBattery = getBattery(stack) ?: return false
            return battery.getAmountAsInt() >= HTItemHelper.getFixedUsage(stack, amount)
        }
    }

    //    Item    //

    override fun isBarVisible(stack: ItemStack): Boolean = hasHandler(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val battery: HTEnergyBattery = getBattery(stack) ?: return 0
        return (13f * battery.getStoredLevelAsFloat()).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = 0xff003f

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        val battery: HTEnergyBattery = getBattery(stack) ?: return
        addEnergyTooltip(battery, tooltipComponents::add)
    }

    override fun isEnchantable(stack: ItemStack): Boolean = stack.maxStackSize == 1 && hasHandler(stack)

    //    User    //

    abstract class User(properties: Properties) : HTEnergyItem(properties) {
        protected abstract val energyUsage: Int

        protected fun canConsumeEnergy(stack: ItemStack): Boolean = canConsumeEnergy(stack, energyUsage)
    }
}
