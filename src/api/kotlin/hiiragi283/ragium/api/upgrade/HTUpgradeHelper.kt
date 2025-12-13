package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

data object HTUpgradeHelper {
    //    HTUpgradeProvider    //

    @JvmStatic
    fun getUpgradeProvider(stack: ImmutableItemStack): HTUpgradeProvider? = stack.getCapability(RagiumCapabilities.UPGRADE_ITEM)

    @JvmStatic
    fun getUpgrade(stack: ImmutableItemStack, key: HTUpgradeKey): Fraction = getUpgradeProvider(stack)?.getUpgradeData(key) ?: Fraction.ZERO

    @JvmStatic
    fun appendTooltips(stack: ItemStack, consumer: Consumer<Component>) {
        val immutable: ImmutableItemStack = stack.toImmutable() ?: return
        val provider: HTUpgradeProvider = getUpgradeProvider(immutable) ?: return
        // Property value
        for (key: HTUpgradeKey in HTUpgradeKey.getAll()) {
            val property: Fraction = provider.getUpgradeData(key)
            if (property <= Fraction.ZERO) continue
            consumer.accept(key.translateColored(ChatFormatting.GRAY, getPropertyColor(key, property), property))
        }
        // Upgrade Group
        provider
            .getGroup()
            ?.let {
                RagiumTranslation.TOOLTIP_UPGRADE_GROUP.translateColored(ChatFormatting.BLUE, ChatFormatting.GRAY, it)
            }?.let(consumer::accept)
    }

    @JvmStatic
    fun appendTooltips(propertyMap: HTUpgradePropertyMap, consumer: Consumer<Component>) {
        propertyMap.forEach { (key: HTUpgradeKey, property: Fraction) ->
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
    fun getItemCapacity(handler: HTUpgradeHandler, base: Int): Int = handler.modifyValue(HTUpgradeKeys.ITEM_CAPACITY) {
        base * it * handler.getBaseMultiplier()
    }

    @JvmStatic
    fun getItemCapacity(stack: ItemStack, base: Int): Int {
        val handler: HTUpgradeHandler = getHandler(stack) ?: return base
        return getItemCapacity(handler, base)
    }

    @JvmStatic
    fun getFluidCapacity(handler: HTUpgradeHandler, base: Int): Int = handler.modifyValue(HTUpgradeKeys.FLUID_CAPACITY) {
        base * it * handler.getBaseMultiplier()
    }

    @JvmStatic
    fun getFluidCapacity(stack: ItemStack, base: Int): Int {
        val handler: HTUpgradeHandler = getHandler(stack) ?: return base
        return getFluidCapacity(handler, base)
    }

    @JvmStatic
    fun getEnergyCapacity(handler: HTUpgradeHandler, base: Int): Int = handler.modifyValue(HTUpgradeKeys.ENERGY_CAPACITY) {
        base * it * handler.getBaseMultiplier()
    }

    @JvmStatic
    fun getEnergyCapacity(stack: ItemStack, base: Int): Int {
        val handler: HTUpgradeHandler = getHandler(stack) ?: return base
        return getEnergyCapacity(handler, base)
    }
}
