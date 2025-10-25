package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack

/**
 * [HTEnergyStorage]に基づいたコンポーネント向けの実装
 */
open class HTComponentEnergyStorage(private val parent: ItemStack, private val capacity: Int) :
    HTEnergyStorage.Mutable(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    protected val component: DataComponentType<Int> get() = RagiumDataComponents.ENERGY

    override fun setAmountAsInt(amount: Int) {
        val fixedAmount: Int = Mth.clamp(amount, 0, capacity)
        parent.setOrRemove(component, fixedAmount) { it <= 0 }
    }

    override fun getAmountAsInt(): Int = Mth.clamp(parent.getOrDefault(component, 0), 0, capacity)

    override fun getCapacityAsInt(): Int = capacity
}
