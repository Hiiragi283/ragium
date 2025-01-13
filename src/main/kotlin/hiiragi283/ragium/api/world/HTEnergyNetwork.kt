package hiiragi283.ragium.api.world

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getOrNull
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

class HTEnergyNetwork(amount: Int) :
    SavedData(),
    IEnergyStorage {
    companion object {
        const val KEY = "network"

        @JvmField
        val ID: ResourceLocation = RagiumAPI.id(KEY)

        @JvmField
        val FACTORY: Factory<HTEnergyNetwork> = Factory(::HTEnergyNetwork, Companion::fromNbt, null)

        @JvmStatic
        fun fromNbt(tag: CompoundTag, registries: HolderLookup.Provider): HTEnergyNetwork = HTEnergyNetwork(tag.getInt(KEY))
    }

    constructor() : this(0)

    override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag =
        CompoundTag().apply { putInt(KEY, delegated.energyStored) }

    fun canConsume(required: Int): Boolean = energyStored >= required

    //    Flag    //

    enum class Flag {
        /**
         * エネルギーを消費します。
         * @see hiiragi283.ragium.api.block.entity.HTMachineBlockEntity.energyFlag
         */
        CONSUME() {
            override fun processAmount(network: HTEnergyNetwork?, amount: Int, simulate: Boolean): Boolean {
                if (network == null) return false
                if (amount <= 0) return false
                return network.extractEnergy(amount, simulate) > 0
            }
        },

        /**
         * エネルギーを生産します。
         * @see hiiragi283.ragium.api.block.entity.HTMachineBlockEntity.energyFlag
         */
        GENERATE() {
            override fun processAmount(network: HTEnergyNetwork?, amount: Int, simulate: Boolean): Boolean {
                if (network == null) return false
                if (amount <= 0) return false
                return network.receiveEnergy(amount, simulate) > 0
            }
        },
        ;

        abstract fun processAmount(network: HTEnergyNetwork?, amount: Int, simulate: Boolean): Boolean

        fun processAmount(network: DataResult<HTEnergyNetwork>, amount: Int, simulate: Boolean): Boolean =
            processAmount(network.getOrNull(), amount, simulate)
    }

    //    IEnergyStorage    //

    private val delegated = EnergyStorage(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, amount)

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = delegated.receiveEnergy(toReceive, simulate)

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = delegated.extractEnergy(toExtract, simulate)

    override fun getEnergyStored(): Int = delegated.energyStored

    override fun getMaxEnergyStored(): Int = delegated.maxEnergyStored

    override fun canExtract(): Boolean = delegated.canExtract()

    override fun canReceive(): Boolean = delegated.canReceive()
}
