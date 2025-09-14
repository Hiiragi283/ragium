package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.util.Mth
import net.neoforged.neoforge.common.MutableDataComponentHolder
import kotlin.math.min

/**
 * [HTEnergyHandler]に基づいたコンポーネント向けの実装
 */
open class HTComponentEnergyStorage(private val parent: MutableDataComponentHolder, capacity: Int) : HTEnergyHandler {
    protected val storage: HTEnergyBattery = createBattery(capacity)

    protected open fun createBattery(capacity: Int): HTEnergyBattery = ComponentStorage(parent, capacity)

    override fun getEnergyHandler(side: Direction?): HTEnergyBattery? = storage

    override fun onContentsChanged() {}

    protected open class ComponentStorage(private val parent: MutableDataComponentHolder, private val capacity: Int) :
        HTEnergyBattery {
        protected val component: DataComponentType<Int> get() = RagiumDataComponents.ENERGY.get()

        override fun getAmount(): Int = Mth.clamp(parent.getOrDefault(component, 0), 0, capacity)

        override fun setAmount(amount: Int) {
            val fixedAmount: Int = Mth.clamp(amount, 0, capacity)
            parent.setOrRemove(component, fixedAmount) { it > 0 }
        }

        override fun getCapacity(): Int = capacity

        override fun insertEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int {
            if (amount <= 0) return 0
            val energyReceived: Int = Mth.clamp(getNeeded(), 0, min(capacity, amount))
            if (!simulate && energyReceived > 0) {
                setAmount(getAmount() + energyReceived)
            }
            return energyReceived
        }

        override fun extractEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int {
            if (amount <= 0) return 0
            val energyExtracted: Int = min(getAmount(), min(capacity, amount))
            if (!simulate && energyExtracted > 0) {
                setAmount(getAmount() - energyExtracted)
            }
            return energyExtracted
        }

        override fun serialize(output: HTValueOutput) {}

        override fun deserialize(input: HTValueInput) {}

        override fun onContentsChanged() {}
    }
}
