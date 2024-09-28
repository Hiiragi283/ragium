package hiiragi283.ragium.common.world

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.util.getState
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.SimpleEnergyStorage

class RagiNetworkManager() :
    PersistentState(),
    EnergyStorage {
    companion object {
        const val KEY = "amount"

        @JvmField
        val TYPE: Type<RagiNetworkManager> = Type(::RagiNetworkManager, ::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): RagiNetworkManager =
            RagiNetworkManager(nbt.getLong(KEY))

        @JvmStatic
        fun getManager(world: ServerWorld): RagiNetworkManager = getState(world, TYPE, Ragium.MOD_ID)

        @JvmStatic
        fun getManagerMap(server: MinecraftServer): Map<RegistryKey<World>, RagiNetworkManager> = server.worlds
            .associateBy(ServerWorld::getRegistryKey)
            .mapValues { getManager(it.value) }
    }

    private val storage: SimpleEnergyStorage = SimpleEnergyStorage(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE)

    constructor(amount: Long) : this() {
        storage.amount = amount
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
