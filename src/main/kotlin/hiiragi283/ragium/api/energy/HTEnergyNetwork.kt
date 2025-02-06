package hiiragi283.ragium.api.energy

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getServerSavedData
import hiiragi283.ragium.api.world.HTSavedDataType
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

val ServerLevel.energyNetwork: HTEnergyNetwork get() = getServerSavedData(HTEnergyNetwork.DATA_FACTORY)

class HTEnergyNetwork(amount: Int) :
    SavedData(),
    IEnergyStorage {
    companion object {
        const val KEY = "network"

        @JvmField
        val DATA_FACTORY: HTSavedDataType<HTEnergyNetwork> =
            HTSavedDataType(RagiumAPI.id(KEY), ::HTEnergyNetwork, ::HTEnergyNetwork)
    }

    constructor() : this(0)

    constructor(tag: CompoundTag) : this(tag.getInt(KEY))

    override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag =
        CompoundTag().apply { putInt(KEY, delegated.energyStored) }

    //    IEnergyStorage    //

    private val delegated = EnergyStorage(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, amount)

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = delegated.receiveEnergy(toReceive, simulate)

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = delegated.extractEnergy(toExtract, simulate)

    override fun getEnergyStored(): Int = delegated.energyStored

    override fun getMaxEnergyStored(): Int = delegated.maxEnergyStored

    override fun canExtract(): Boolean = delegated.canExtract()

    override fun canReceive(): Boolean = delegated.canReceive()
}
