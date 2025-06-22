package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.util.HTSavedDataType
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

internal class HTEnergyNetwork(var amount: Int, var capacity: Int) :
    SavedData(),
    IEnergyStorage {
    companion object {
        @JvmStatic
        private fun getInitialCapacity(): Int = RagiumConfig.COMMON.defaultNetworkCapacity.get()

        @JvmField
        val DATA_FACTORY: HTSavedDataType<HTEnergyNetwork> =
            HTSavedDataType(RagiumAPI.id(RagiumConstantValues.NETWORK), ::HTEnergyNetwork, ::fromTag)

        @JvmStatic
        private fun fromTag(tag: CompoundTag, provider: HolderLookup.Provider): HTEnergyNetwork {
            var capacity: Int = tag.getInt(RagiumConstantValues.ENERGY_CAPACITY)
            if (capacity <= 0) {
                capacity = getInitialCapacity()
            }
            return HTEnergyNetwork(tag.getInt(RagiumConstantValues.ENERGY_STORED), capacity)
        }
    }

    constructor() : this(0, getInitialCapacity())

    override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag = buildNbt {
        putInt(RagiumConstantValues.ENERGY_STORED, amount)
        if (capacity <= 0) {
            capacity = getInitialCapacity()
        }
        putInt(RagiumConstantValues.ENERGY_CAPACITY, capacity)
    }

    //    IEnergyStorage    //

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!canReceive() || toReceive <= 0) return 0
        val received: Int = Mth.clamp(maxEnergyStored - energyStored, 0, toReceive)
        if (!simulate) {
            this.amount += received
            setDirty()
        }
        return received
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!canExtract() || toExtract <= 0) return 0
        val extracted: Int = min(energyStored, toExtract)
        if (!simulate) {
            this.amount -= extracted
            setDirty()
        }
        return extracted
    }

    override fun getEnergyStored(): Int = amount

    override fun getMaxEnergyStored(): Int = capacity

    override fun canExtract(): Boolean = true

    override fun canReceive(): Boolean = true
}
