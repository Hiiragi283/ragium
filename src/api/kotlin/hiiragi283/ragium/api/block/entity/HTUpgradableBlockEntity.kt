package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.tier.HTBaseTier
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.level.ItemLike

interface HTUpgradableBlockEntity {
    fun hasUpgrade(item: ItemLike): Boolean

    fun <T : Any> getUpgradeData(type: DataComponentType<T>): List<T>

    fun getUpgradeDataType(): DataComponentType<HTMachineUpgrade>

    fun <T : Any> getFirstUpgradeData(type: DataComponentType<T>): T? = getUpgradeData(type).firstOrNull()

    fun getMaxMachineTier(): HTBaseTier? = getUpgradeData(getUpgradeDataType()).maxOfOrNull(HTMachineUpgrade::base)

    fun collectModifier(key: HTMachineUpgrade.Key): Float {
        var result = 1f
        for (upgrade: HTMachineUpgrade in getUpgradeData(getUpgradeDataType())) {
            if (upgrade.base == HTBaseTier.CREATIVE) continue
            val multiplier: Float = upgrade.getProperty(key) ?: continue
            result *= multiplier
        }
        return result
    }

    fun calculateValue(base: Int, key: HTMachineUpgrade.Key): Int {
        var result = 1f
        for (upgrade: HTMachineUpgrade in getUpgradeData(getUpgradeDataType())) {
            if (upgrade.base == HTBaseTier.CREATIVE) {
                return key.creativeValue
            }
            val multiplier: Float = upgrade.getProperty(key) ?: continue
            result *= multiplier
        }
        return (base * result).toInt()
    }
}
