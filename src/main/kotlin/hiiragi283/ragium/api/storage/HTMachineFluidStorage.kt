package hiiragi283.ragium.api.storage

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTUnitResult
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.fabricmc.fabric.impl.transfer.TransferApiImpl
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class HTMachineFluidStorage private constructor(size: Int, private val filter: (Int, FluidVariant) -> Boolean, tier: HTMachineTier) :
    SlottedStorage<FluidVariant> {
        companion object {
            const val NBT_KEY = "fluid_storages"
        }

        constructor(builder: HTStorageBuilder, tier: HTMachineTier) : this(
            builder.size,
            builder.fluidFilter,
            tier,
        )

        private var callback: () -> Unit = {}

        fun setCallback(callback: () -> Unit): HTMachineFluidStorage = apply {
            this.callback = callback
        }

        private val parts: Array<SingleFluidStorage> = Array(size) { slot: Int -> childStorage(slot, tier) }

        private fun childStorage(slot: Int, tier: HTMachineTier): SingleFluidStorage = object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

            override fun canInsert(variant: FluidVariant): Boolean = filter(slot, variant)

            override fun onFinalCommit() {
                callback()
            }
        }

        fun getStorage(index: Int): DataResult<SingleFluidStorage> = parts.getOrNull(index).toDataResult { "Invalid child index: $index" }

        fun <T : Any> map(index: Int, transform: (SingleFluidStorage) -> T?): DataResult<T> = getStorage(index).map(transform)

        fun <T : Any> flatMap(index: Int, transform: (SingleFluidStorage) -> DataResult<T>): DataResult<T> =
            getStorage(index).flatMap(transform)

        fun unitMap(index: Int, transform: (SingleFluidStorage) -> HTUnitResult): HTUnitResult = getStorage(index).unitMap(transform)

        fun getVariantStack(index: Int): HTFluidVariantStack =
            map(index, SingleFluidStorage::variantStack).result().orElse(HTFluidVariantStack.EMPTY)

        fun interactByPlayer(player: PlayerEntity): Boolean = FluidStorageUtil.interactWithFluidStorage(this, player, Hand.MAIN_HAND)

        fun update(tier: HTMachineTier): HTMachineFluidStorage = apply {
            val copied: Array<SingleFluidStorage> = parts.copyOf()
            copied.forEachIndexed { index: Int, storage: SingleFluidStorage ->
                val newStorage: SingleFluidStorage = childStorage(index, tier)
                storage.copyTo(newStorage)
                parts[index] = newStorage
            }
        }

        fun findInsertableStorage(variant: FluidVariant): SingleFluidStorage? {
            val insertableStorages: List<SingleFluidStorage> = parts.filter(SingleFluidStorage::supportsInsertion)
            return insertableStorages.find { storageIn: SingleFluidStorage -> storageIn.resource == variant }
                ?: insertableStorages.find(SingleFluidStorage::isResourceBlank)
        }

        fun findExtractableStorage(variant: FluidVariant): SingleFluidStorage? {
            val extractableStorages: List<SingleFluidStorage> = parts.filter(SingleFluidStorage::supportsExtraction)
            return extractableStorages.find { storageIn: SingleFluidStorage -> storageIn.resource == variant }
        }

        fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup, tier: HTMachineTier = HTMachineTier.PRIMITIVE) {
            val list: NbtList = nbt.getList(NBT_KEY, NbtElement.COMPOUND_TYPE.toInt())
            update(tier)
            list.forEachIndexed { index: Int, nbtElement: NbtElement ->
                if (nbtElement is NbtCompound) {
                    parts.getOrNull(index)?.readNbt(nbtElement, wrapperLookup)
                }
            }
        }

        fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            callback()
            nbt.put(
                NBT_KEY,
                buildNbtList {
                    parts.forEach { add(buildNbt { it.writeNbt(this, wrapperLookup) }) }
                },
            )
        }

        //    HTFluidSyncable    //

        fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit, vararg slotRange: Int) {
            slotRange.forEach { index: Int ->
                parts.getOrNull(index)?.let { storage: SingleFluidStorage ->
                    sender(player, index, storage.variant, storage.amount)
                }
            }
        }

        fun sendPacket(
            player: ServerPlayerEntity,
            sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit,
            slotRange: IntRange = parts.indices,
        ) {
            sendPacket(player, sender, *slotRange.toList().toIntArray())
        }

        //    SlottedStorage    //

        override fun insert(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long =
            findInsertableStorage(resource)?.insert(resource, maxAmount, transaction) ?: 0

        override fun extract(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long =
            findExtractableStorage(resource)?.extract(resource, maxAmount, transaction) ?: 0

        @Suppress("UnstableApiUsage")
        override fun iterator(): MutableIterator<StorageView<FluidVariant>> = TransferApiImpl.makeListView(this).iterator()

        override fun getSlotCount(): Int = parts.size

        override fun getSlot(slot: Int): SingleSlotStorage<FluidVariant> = getStorage(slot).result().orElseThrow()
    }
