package hiiragi283.ragium.api.world

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.energy.HTEnergyType
import hiiragi283.ragium.api.energy.HTSingleEnergyStorage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState

class HTEnergyNetwork() :
    PersistentState(),
    SingleSlotStorage<HTEnergyType> {
    companion object {
        const val KEY = "network"

        @JvmField
        val ID: Identifier = RagiumAPI.id(KEY)

        @JvmField
        val TYPE: Type<HTEnergyNetwork> = Type(::HTEnergyNetwork, Companion::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): HTEnergyNetwork = HTEnergyNetwork(
            nbt.getLong(
                KEY,
            ),
        )
    }

    private val storage: HTSingleEnergyStorage =
        HTSingleEnergyStorage.ofFiltered(Long.MAX_VALUE, HTEnergyType.ELECTRICITY)

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

    //    Storage    //

    override fun iterator(): MutableIterator<StorageView<HTEnergyType>> = storage.iterator()

    override fun extract(resource: HTEnergyType, maxAmount: Long, transaction: TransactionContext): Long = when (resource) {
        HTEnergyType.ELECTRICITY -> storage.extract(resource, maxAmount, transaction)
        else -> 0
    }

    override fun insert(resource: HTEnergyType, maxAmount: Long, transaction: TransactionContext): Long = when (resource) {
        HTEnergyType.ELECTRICITY -> storage.insert(resource, maxAmount, transaction)
        else -> 0
    }

    override fun isResourceBlank(): Boolean = false

    override fun getResource(): HTEnergyType = HTEnergyType.ELECTRICITY

    override fun getAmount(): Long = storage.amount

    override fun getCapacity(): Long = storage.capacity
}
