package hiiragi283.ragium.api.machine

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.core.component.DataComponentGetter
import net.neoforged.neoforge.common.MutableDataComponentHolder

data object HTMachineUpgradeHelper {
    @JvmStatic
    fun getUpgrades(getter: DataComponentGetter): HTMachineUpgradeList =
        getter.getOrDefault(RagiumDataComponents.UPGRADES, HTMachineUpgradeList.EMPTY)

    @JvmStatic
    fun getCombinedUpgrade(getter: DataComponentGetter): HTMachineUpgrade = getUpgrades(getter).combinedUpgrade

    @JvmStatic
    fun setUpgrades(holder: MutableDataComponentHolder, upgradeList: HTMachineUpgradeList.Mutable) {
        setUpgrades(holder, upgradeList.toImmutable())
    }

    @JvmStatic
    fun setUpgrades(holder: MutableDataComponentHolder, upgradeList: HTMachineUpgradeList) {
        holder.set(RagiumDataComponents.UPGRADES, upgradeList.takeUnless(HTMachineUpgradeList::isEmpty))
    }

    @JvmStatic
    fun modifySpeed(getter: DataComponentGetter, base: Int): Int {
        val multiplier: Long = maxOf(1, 100L + getCombinedUpgrade(getter).speed)
        return Ints.saturatedCast(base * multiplier / 100)
    }

    @JvmStatic
    fun modifyEnergy(getter: DataComponentGetter, base: Int): Int {
        val multiplier: Long = maxOf(1, 100L + getCombinedUpgrade(getter).efficiency)
        return Ints.saturatedCast(base * 100 / multiplier)
    }

    @JvmStatic
    fun modifyCapacity(getter: DataComponentGetter, base: Int): Int {
        val multiplier: Long = maxOf(1, 100L + getCombinedUpgrade(getter).capacity)
        return Ints.saturatedCast(base * multiplier / 100)
    }
}
