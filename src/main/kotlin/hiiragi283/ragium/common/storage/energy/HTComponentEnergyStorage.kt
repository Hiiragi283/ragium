package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.function.clamp
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * [HTEnergyStorage]に基づいたコンポーネント向けの実装
 */
open class HTComponentEnergyStorage(private val parent: ItemStack, private val capacity: Int) :
    HTEnergyStorage.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    protected val component: DataComponentType<Int> get() = RagiumDataComponents.ENERGY

    override fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
        if (amount == 0) {
            if (parent.getOrDefault(component, 0) == 0) return
            parent.remove(component)
        } else if (!validate) {
            parent.set(component, min(amount, getCapacity()))
        } else {
            error("Invalid amount for storage: $amount")
        }
        onContentsChanged()
    }

    override fun getAmount(): Int = parent.getOrDefault(component, 0).clamp(0..capacity)

    override fun getCapacity(): Int = capacity
}
