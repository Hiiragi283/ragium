package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.extension.addEnergyTooltip
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.roundToInt

/**
 * @see [de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy]
 */
abstract class HTEnergyItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getStorage(stack: ItemStack): IEnergyStorage? = stack.getCapability(Capabilities.EnergyStorage.ITEM)

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
        fun getEnergyUsage(stack: ItemStack, amount: Int): Int {
            var result: Int = amount
            val enchGetter: HolderGetter<Enchantment> = CommonHooks.resolveLookup(Registries.ENCHANTMENT) ?: return amount
            enchGetter
                .get(Enchantments.UNBREAKING)
                .ifPresent { holder: Holder.Reference<Enchantment> ->
                    val level: Int = stack.getEnchantmentLevel(holder)
                    if (level > 0) {
                        result /= (level + 1)
                    }
                }
            return result
        }

        @JvmStatic
        fun canConsumeEnergy(stack: ItemStack, amount: Int): Boolean = getEnergyStored(stack) >= getEnergyUsage(stack, amount)
    }

    //    Item    //

    override fun isBarVisible(stack: ItemStack): Boolean = true

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

        protected fun getEnergyUsage(stack: ItemStack): Int = getEnergyUsage(stack, energyUsage)

        protected fun canConsumeEnergy(stack: ItemStack): Boolean = canConsumeEnergy(stack, energyUsage)
    }
}
