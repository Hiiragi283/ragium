package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.storage.energy.IEnergyStorageModifiable
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.util.Mth
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

/**
 * [HTEnergyHandler]に基づいたコンポーネント向けの実装
 */
open class HTComponentEnergyStorage(private val parent: MutableDataComponentHolder, capacity: Int) : HTEnergyHandler {
    protected val storage: IEnergyStorage = createStorage(capacity)

    protected open fun createStorage(capacity: Int): IEnergyStorage = ComponentStorage(parent, capacity)

    override fun getEnergyHandler(side: Direction?): IEnergyStorage? = storage

    override fun onContentsChanged() {}

    protected open class ComponentStorage(private val parent: MutableDataComponentHolder, private val capacity: Int) :
        IEnergyStorageModifiable {
        protected val component: DataComponentType<Int> get() = RagiumDataComponents.ENERGY.get()

        override fun setEnergyStored(amount: Int) {
            val fixedAmount: Int = Mth.clamp(amount, 0, capacity)
            if (fixedAmount > 0) {
                parent.set(component, fixedAmount)
            } else {
                parent.remove(component)
            }
        }

        override fun setMaxEnergyStored(capacity: Int) {}

        override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
            if (!canReceive() || toReceive <= 0) return 0
            val energyReceived: Int = Mth.clamp(getNeeded(), 0, min(capacity, toReceive))
            if (!simulate && energyReceived > 0) {
                energyStored += energyReceived
            }
            return energyReceived
        }

        override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
            if (!canExtract() || toExtract <= 0) return 0
            val energyExtracted: Int = min(energyStored, min(capacity, toExtract))
            if (!simulate && energyExtracted > 0) {
                energyStored -= energyExtracted
            }
            return energyExtracted
        }

        override fun getEnergyStored(): Int = Mth.clamp(parent.getOrDefault(component, 0), 0, capacity)

        override fun getMaxEnergyStored(): Int = capacity

        override fun canExtract(): Boolean = true

        override fun canReceive(): Boolean = true
    }
}
