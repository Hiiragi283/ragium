package hiiragi283.lib.transfer.energy

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.redstone.Redstone
import net.neoforged.neoforge.transfer.access.ItemAccess
import kotlin.math.roundToInt

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 * @see net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil
 */
data object HTEnergyStorageUtil {
    @JvmStatic
    fun calculateRedstoneLevel(view: HTEnergyView): Int = Mth.lerpDiscrete(view.filledLevel, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    @JvmStatic
    fun getEnergy(container: DataComponentGetter): Int = container.getOrDefault(RagiumDataComponents.ENERGY, 0)

    @JvmStatic
    fun createUpdatedPatch(newAmount: Int): DataComponentPatch = buildDataPatch {
        if (newAmount <= 0) {
            remove(RagiumDataComponents.ENERGY)
        } else {
            set(RagiumDataComponents.ENERGY, newAmount)
        }
    }

    /**
     * @since 26.1.8
     */
    @JvmStatic
    fun updateEnergy(container: DataComponentMap.Builder, newAmount: Int) {
        container.set(RagiumDataComponents.ENERGY, newAmount.takeIf { it > 0 })
    }

    @JvmStatic
    inline fun addEnergyTooltip(view: HTEnergyView, consumer: (Text) -> Unit, isCreative: Boolean) {
        // Empty name if amount is not positive
        if (view.isEmpty) {
            consumer(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        when {
            isCreative -> HTCommonTranslation.STORED_FE.translate(HTCommonTranslation.INFINITE)
            else -> HTCommonTranslation.STORED_FE.translate(HTCommonTranslation.FRACTION.translate(view.amount, view.capacity))
        }.let(consumer)
    }

    @JvmStatic
    fun getEnergyBarWidth(container: ItemStack): Int = when {
        container.count > 1 -> 0
        else -> (13.0 - 13.0 * getEnergyDurability(container)).roundToInt()
    }

    @JvmStatic
    private fun getEnergyDurability(container: ItemStack): Double {
        val handler: HTEnergyStorage = HTEnergyCapabilities.getStorage(ItemAccess.forStack(container)) ?: return 1.0
        return 1.0 - handler.filledLevel
    }
}
