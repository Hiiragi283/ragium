package hiiragi283.ragium.common.storage.energy

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.util.HTSavedDataType
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.ExtraCodecs
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
        val CODEC: Codec<HTEnergyNetwork> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_INT
                        .optionalFieldOf(RagiumConstantValues.ENERGY_CAPACITY, getInitialCapacity())
                        .forGetter(HTEnergyNetwork::capacity),
                    ExtraCodecs.NON_NEGATIVE_INT
                        .optionalFieldOf(RagiumConstantValues.ENERGY_STORED, 0)
                        .forGetter(HTEnergyNetwork::amount),
                ).apply(instance, ::HTEnergyNetwork)
        }

        @JvmField
        val DATA_FACTORY: HTSavedDataType<HTEnergyNetwork> =
            HTSavedDataType.create(RagiumAPI.id(RagiumConstantValues.NETWORK), CODEC, ::HTEnergyNetwork)
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
