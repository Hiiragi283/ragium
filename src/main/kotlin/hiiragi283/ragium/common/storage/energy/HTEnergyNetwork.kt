package hiiragi283.ragium.common.storage.energy

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.storage.energy.IEnergyStorageModifiable
import hiiragi283.ragium.api.util.HTSavedDataType
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.Mth
import net.minecraft.world.level.saveddata.SavedData
import kotlin.math.min

internal class HTEnergyNetwork(private var amount: Int, private var capacity: Int) :
    SavedData(),
    IEnergyStorageModifiable {
    companion object {
        @JvmStatic
        private fun getInitialCapacity(): Int = RagiumAPI.getConfig().getDefaultNetworkCapacity()

        @JvmField
        val CODEC: Codec<HTEnergyNetwork> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf(RagiumConst.ENERGY_STORED, 0).forGetter(HTEnergyNetwork::amount),
                    ExtraCodecs.POSITIVE_INT
                        .optionalFieldOf(
                            RagiumConst.ENERGY_CAPACITY,
                            getInitialCapacity(),
                        ).forGetter(HTEnergyNetwork::capacity),
                ).apply(instance, ::HTEnergyNetwork)
        }

        @JvmField
        val DATA_FACTORY: HTSavedDataType<HTEnergyNetwork> =
            HTSavedDataType.create(RagiumAPI.id(RagiumConst.NETWORK), CODEC, ::HTEnergyNetwork)
    }

    constructor() : this(0, getInitialCapacity())

    init {
        amount = min(amount, capacity)
    }

    override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag = buildNbt {
        putInt(RagiumConst.ENERGY_STORED, amount)
        putInt(RagiumConst.ENERGY_CAPACITY, capacity)
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

    override fun setEnergyStored(amount: Int) {
        this.amount = amount
    }

    override fun getMaxEnergyStored(): Int = capacity

    override fun setMaxEnergyStored(capacity: Int) {
        this.capacity = capacity
    }

    override fun canExtract(): Boolean = true

    override fun canReceive(): Boolean = true
}
