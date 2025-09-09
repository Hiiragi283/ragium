package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.addEnergyTooltip
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.common.util.HTItemHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.roundToInt

/**
 * @see [de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy]
 */
abstract class HTEnergyItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getStorage(stack: ItemStack): IEnergyStorage? = HTMultiCapability.ENERGY.getCapability(stack)

        @JvmStatic
        fun hasStorage(stack: ItemStack): Boolean = getStorage(stack) != null

        @JvmStatic
        fun extractEnergy(stack: ItemStack, maxExtract: Int, simulate: Boolean): Int =
            getStorage(stack)?.extractEnergy(maxExtract, simulate) ?: 0

        @JvmStatic
        fun getEnergyStored(stack: ItemStack): Int = getStorage(stack)?.energyStored ?: 0

        @JvmStatic
        fun getMaxEnergyStored(stack: ItemStack): Int = getStorage(stack)?.maxEnergyStored ?: 0

        @JvmStatic
        fun canConsumeEnergy(stack: ItemStack, amount: Int): Boolean = getEnergyStored(stack) >= HTItemHelper.getFixedUsage(stack, amount)
    }

    //    Item    //

    override fun isBarVisible(stack: ItemStack): Boolean = hasStorage(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val storage: IEnergyStorage = getStorage(stack) ?: return 0
        return (13f / storage.maxEnergyStored * storage.energyStored).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = 0xff003f

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        addEnergyTooltip(stack, tooltipComponents::add)
    }

    //    User    //

    abstract class User(properties: Properties) : HTEnergyItem(properties) {
        protected abstract val energyUsage: Int

        protected fun getEnergyUsage(stack: ItemStack): Int = HTItemHelper.getFixedUsage(stack, energyUsage)

        protected fun canConsumeEnergy(stack: ItemStack): Boolean = canConsumeEnergy(stack, energyUsage)
    }
}
