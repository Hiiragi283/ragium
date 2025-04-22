package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.util.HTSavedDataType
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

internal class HTEnergyNetwork(private var amount: Int = 0) :
    SavedData(),
    IEnergyStorage {
    companion object {
        const val KEY = "network"

        @JvmField
        val DATA_FACTORY: HTSavedDataType<HTEnergyNetwork> =
            HTSavedDataType(RagiumAPI.id(KEY), ::HTEnergyNetwork, ::HTEnergyNetwork)
    }

    constructor() : this(0)

    constructor(tag: CompoundTag, provider: HolderLookup.Provider) : this(tag.getInt(KEY))

    override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag = buildNbt {
        putInt(KEY, amount)
    }

    //    IEnergyStorage    //

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!canReceive() || toReceive <= 0) return 0
        val received: Int = Mth.clamp(maxEnergyStored - energyStored, 0, toReceive)
        if (!simulate) this.amount += received
        return received
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!canExtract() || toExtract <= 0) return 0
        val extracted: Int = min(energyStored, toExtract)
        if (!simulate) this.amount -= extracted
        return extracted
    }

    override fun getEnergyStored(): Int = amount

    override fun getMaxEnergyStored(): Int = Int.MAX_VALUE

    override fun canExtract(): Boolean = true

    override fun canReceive(): Boolean = true
}
