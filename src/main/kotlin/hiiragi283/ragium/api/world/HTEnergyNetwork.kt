package hiiragi283.ragium.api.world

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.useTransaction
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.SimpleEnergyStorage

class HTEnergyNetwork() :
    PersistentState(),
    EnergyStorage {
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

    private val storage = SimpleEnergyStorage(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE)

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

    fun canConsume(required: Long): Boolean = amount >= required

    //    Flag    //

    enum class Flag {
        /**
         * エネルギーを消費します。
         * @see hiiragi283.ragium.api.block.HTMachineBlockEntityBase.energyFlag
         */
        CONSUME() {
            override fun processAmount(network: HTEnergyNetwork?, amount: Long, parent: TransactionContext?): Boolean {
                if (network == null) return false
                if (amount <= 0) return false
                useTransaction(parent) { transaction: Transaction ->
                    val extracted: Long = network.extract(amount, transaction)
                    if (extracted == amount) {
                        transaction.commit()
                        return true
                    }
                }
                return false
            }
        },

        /**
         * エネルギーを生産します。
         * @see hiiragi283.ragium.api.block.HTMachineBlockEntityBase.energyFlag
         */
        GENERATE() {
            override fun processAmount(network: HTEnergyNetwork?, amount: Long, parent: TransactionContext?): Boolean {
                if (network == null) return false
                if (amount <= 0) return false
                useTransaction(parent) { transaction: Transaction ->
                    val inserted: Long = network.insert(amount, transaction)
                    if (inserted == amount) {
                        transaction.commit()
                        return true
                    }
                }
                return false
            }
        },
        ;

        abstract fun processAmount(network: HTEnergyNetwork?, amount: Long, parent: TransactionContext? = null): Boolean
    }

    //    Storage    //

    override fun extract(maxAmount: Long, transaction: TransactionContext): Long = storage.extract(maxAmount, transaction)

    override fun insert(maxAmount: Long, transaction: TransactionContext): Long = storage.insert(maxAmount, transaction)

    override fun getAmount(): Long = storage.amount

    override fun getCapacity(): Long = storage.capacity
}
