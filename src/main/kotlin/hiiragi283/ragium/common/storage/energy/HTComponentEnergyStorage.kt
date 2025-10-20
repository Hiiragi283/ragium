package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.util.Mth
import net.neoforged.neoforge.common.MutableDataComponentHolder

/**
 * [HTEnergyHandler]に基づいたコンポーネント向けの実装
 */
open class HTComponentEnergyStorage(private val parent: MutableDataComponentHolder, capacity: Long) : HTEnergyHandler {
    protected val storage: HTEnergyBattery = createBattery(capacity)

    protected open fun createBattery(capacity: Long): HTEnergyBattery = ComponentStorage(parent, capacity)

    override fun getEnergyBattery(side: Direction?): HTEnergyBattery? = storage

    protected open class ComponentStorage(private val parent: MutableDataComponentHolder, private val capacity: Long) :
        HTEnergyBattery.Mutable,
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        protected val component: DataComponentType<Long> get() = RagiumDataComponents.ENERGY

        override fun setAmountAsLong(amount: Long) {
            val fixedAmount: Long = Mth.clamp(amount, 0, capacity)
            parent.setOrRemove(component, fixedAmount) { it > 0 }
        }

        override fun getAmountAsLong(): Long = Mth.clamp(parent.getOrDefault(component, 0), 0, capacity)

        override fun getCapacityAsLong(): Long = capacity
    }
}
