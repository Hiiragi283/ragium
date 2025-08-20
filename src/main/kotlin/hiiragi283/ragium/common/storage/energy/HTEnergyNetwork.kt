package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.storage.energy.IEnergyStorageModifiable
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.neoforged.neoforge.common.util.INBTSerializable
import kotlin.math.min

class HTEnergyNetwork(private var amount: Int, private var capacity: Int) :
    IEnergyStorageModifiable,
    INBTSerializable<CompoundTag> {
    companion object {
        @JvmStatic
        private fun getInitialCapacity(): Int = RagiumAPI.getConfig().getDefaultNetworkCapacity()
    }

    constructor() : this(0, getInitialCapacity())

    override fun setEnergyStored(amount: Int) {
        this.amount = amount
    }

    override fun setMaxEnergyStored(capacity: Int) {
        this.capacity = capacity
    }

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!canReceive() || toReceive <= 0) return 0
        val received: Int = Mth.clamp(maxEnergyStored - energyStored, 0, toReceive)
        if (!simulate) {
            this.amount += received
        }
        return received
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!canExtract() || toExtract <= 0) return 0
        val extracted: Int = min(energyStored, toExtract)
        if (!simulate) {
            this.amount -= extracted
        }
        return extracted
    }

    override fun getEnergyStored(): Int = amount

    override fun getMaxEnergyStored(): Int = capacity

    override fun canExtract(): Boolean = true

    override fun canReceive(): Boolean = true

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        putInt(RagiumConst.ENERGY_STORED, amount)
        putInt(RagiumConst.ENERGY_CAPACITY, capacity)
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        val capacity: Int = nbt.getInt(RagiumConst.ENERGY_CAPACITY)
        this.capacity = if (capacity <= 0) getInitialCapacity() else capacity
        this.amount = nbt.getInt(RagiumConst.ENERGY_STORED)
    }
}
