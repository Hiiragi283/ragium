package hiiragi283.ragium.common.world

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.util.getState
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.SimpleEnergyStorage

val MinecraftServer.networkMap: Map<ServerWorld, HTEnergyNetwork>
    get() = worlds.associateWith { it.energyNetwork }

val ServerWorld.energyNetwork: HTEnergyNetwork
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID)

val World.energyNetwork: HTEnergyNetwork?
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID)

class HTEnergyNetwork() :
    PersistentState(),
    EnergyStorage {
    companion object {
        const val KEY = "network"

        @JvmField
        val ID: Identifier = Ragium.id(KEY)

        @JvmField
        val TYPE: Type<HTEnergyNetwork> = Type(::HTEnergyNetwork, ::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): HTEnergyNetwork = HTEnergyNetwork(nbt.getLong(KEY))
    }

    private val storage: SimpleEnergyStorage = SimpleEnergyStorage(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE)

    constructor(amount: Long) : this() {
        storage.amount = amount
    }

    fun setAmount(value: Long) {
        storage.amount = value
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): NbtCompound {
        nbt.putLong(KEY, storage.amount)
        return nbt
    }

    //    EnergyStorage    //

    override fun insert(maxAmount: Long, transaction: TransactionContext): Long = storage.insert(maxAmount, transaction)

    override fun extract(maxAmount: Long, transaction: TransactionContext): Long = storage.extract(maxAmount, transaction)

    override fun getAmount(): Long = storage.amount

    override fun getCapacity(): Long = storage.capacity
}
