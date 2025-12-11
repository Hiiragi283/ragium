package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.item.component.HTComponentUpgrade
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

data object HTUpgradeHelper {
    //    HTUpgradeProvider    //

    @JvmStatic
    fun getUpgrade(stack: ItemStack, key: HTUpgradeKey): Fraction? {
        val immutable: ImmutableItemStack = stack.toImmutable() ?: return Fraction.ZERO
        return getUpgrade(immutable, key)
    }

    @JvmStatic
    fun getUpgrade(stack: ImmutableItemStack, key: HTUpgradeKey): Fraction =
        stack.getCapability(RagiumCapabilities.UPGRADE_ITEM)?.getUpgradeData(key) ?: Fraction.ZERO

    @JvmStatic
    fun appendTooltips(stack: ItemStack, consumer: Consumer<Component>) {
        for (key: HTUpgradeKey in HTUpgradeKey.getAll()) {
            val property: Fraction = getUpgrade(stack, key) ?: continue
            if (property <= Fraction.ZERO) continue
            consumer.accept(key.translateColored(ChatFormatting.GRAY, getPropertyColor(key, property), property))
        }
    }

    @JvmStatic
    fun appendTooltips(component: HTComponentUpgrade, consumer: Consumer<Component>) {
        component.forEach { (key: HTUpgradeKey, property: Fraction) ->
            consumer.accept(key.translateColored(ChatFormatting.GRAY, getPropertyColor(key, property), property))
        }
    }

    @JvmStatic
    private fun getPropertyColor(key: HTUpgradeKey, property: Fraction): ChatFormatting = when {
        property > Fraction.ONE -> ChatFormatting.GREEN
        property < Fraction.ONE -> ChatFormatting.RED
        else -> ChatFormatting.WHITE
    }

    //    HTUpgradableHandler    //

    @JvmStatic
    fun getHandler(stack: ItemStack): HTUpgradeHandler? = stack.toImmutable()?.let(::getHandler)

    @JvmStatic
    fun getHandler(stack: ImmutableItemStack): HTUpgradeHandler? = stack.getCapability(RagiumCapabilities.UPGRADABLE_ITEM)

    @JvmStatic
    fun getTankCapacity(handler: HTUpgradeHandler, base: Int): Int = handler.modifyValue(RagiumUpgradeKeys.FLUID_CAPACITY) {
        base * it * handler.getBaseMultiplier()
    }

    @JvmStatic
    fun getTankCapacity(stack: ItemStack, base: Int): Int {
        val handler: HTUpgradeHandler = getHandler(stack) ?: return base
        return getTankCapacity(handler, base)
    }

    @JvmStatic
    fun getEnergyCapacity(handler: HTUpgradeHandler, base: Int): Int = handler.modifyValue(RagiumUpgradeKeys.ENERGY_CAPACITY) {
        base * it * handler.getBaseMultiplier()
    }

    @JvmStatic
    fun getEnergyCapacity(stack: ItemStack, base: Int): Int {
        val handler: HTUpgradeHandler = getHandler(stack) ?: return base
        return getEnergyCapacity(handler, base)
    }
}
