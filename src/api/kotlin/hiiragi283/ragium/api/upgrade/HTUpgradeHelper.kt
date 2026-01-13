package hiiragi283.ragium.api.upgrade

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.times
import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

data object HTUpgradeHelper {
    //    HTUpgradeProvider    //

    @JvmStatic
    fun getUpgrade(resource: HTItemResourceType, key: HTUpgradeKey): Fraction? = RagiumDataMapTypes.getUpgradeData(resource)?.get(key)

    @JvmStatic
    fun appendTooltips(propertyMap: HTUpgradePropertyMap, consumer: Consumer<Component>) {
        propertyMap.forEach { (key: HTUpgradeKey, property: Fraction) ->
            consumer.accept(key.translateColored(HTDefaultColor.GRAY, getPropertyColor(key, property), property))
        }
    }

    @JvmStatic
    fun getPropertyColor(key: HTUpgradeKey, property: Fraction): HTDefaultColor = when {
        property > Fraction.ONE -> HTDefaultColor.GREEN
        property < Fraction.ONE -> HTDefaultColor.RED
        else -> HTDefaultColor.WHITE
    }

    //    HTUpgradableHandler    //

    @JvmStatic
    fun getHandler(stack: ItemStack): HTUpgradeHandler? = stack.getCapability(RagiumCapabilities.UPGRADABLE_ITEM)

    @JvmStatic
    fun isCreative(stack: ItemStack): Boolean = getHandler(stack)?.isCreative() ?: false

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
