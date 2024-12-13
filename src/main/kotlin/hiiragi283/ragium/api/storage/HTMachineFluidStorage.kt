package hiiragi283.ragium.api.storage

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.fabricmc.fabric.impl.transfer.TransferApiImpl
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity

class HTMachineFluidStorage(size: Int, private val slotMap: Map<Int, HTStorageIO>, tier: HTMachineTier) : SlottedStorage<FluidVariant> {
    companion object {
        const val NBT_KEY = "fluid_storages"

        @JvmStatic
        fun ofSimple(machine: HTMachineBlockEntityBase): HTMachineFluidStorage = Builder(2)
            .input(0)
            .output(1)
            .build(machine.tier)
            .setCallback(machine::markDirty)

        @JvmStatic
        fun ofSmall(machine: HTMachineBlockEntityBase): HTMachineFluidStorage = Builder(4)
            .input(0, 1)
            .output(2, 3)
            .build(machine.tier)
            .setCallback(machine::markDirty)
    }

    private var callback: () -> Unit = {}

    fun setCallback(callback: () -> Unit): HTMachineFluidStorage = apply {
        this.callback = callback
    }

    private val parts: Array<SingleFluidStorage> =
        Array(size) { slot: Int -> SingleFluidStorage.withFixedCapacity(tier.tankCapacity, callback) }

    fun getStorage(index: Int): DataResult<SingleFluidStorage> = parts.getOrNull(index).toDataResult { "Invalid child index: $index" }

    fun <T : Any> map(index: Int, transform: (SingleFluidStorage) -> T?): DataResult<T> = getStorage(index).map(transform)

    fun getVariantStack(index: Int): HTFluidVariantStack =
        map(index, SingleFluidStorage::variantStack).result().orElse(HTFluidVariantStack.EMPTY)

    fun update(tier: HTMachineTier): HTMachineFluidStorage = apply {
        val copied: Array<SingleFluidStorage> = parts.copyOf()
        copied.forEachIndexed { index: Int, storage: SingleFluidStorage ->
            val newStorage: SingleFluidStorage = SingleFluidStorage.withFixedCapacity(tier.tankCapacity, callback)
            storage.copyTo(newStorage)
            parts[index] = newStorage
        }
    }

    private fun getStorageIO(slot: Int): HTStorageIO = slotMap.getOrDefault(slot, HTStorageIO.INTERNAL)

    fun findInsertableStorage(variant: FluidVariant): SingleFluidStorage? {
        val insertableStorages: List<SingleFluidStorage> =
            parts.filterIndexed { slot: Int, _: SingleFluidStorage -> getStorageIO(slot).canInsert }
        return insertableStorages.find { storageIn: SingleFluidStorage -> storageIn.resource == variant }
            ?: insertableStorages.find(SingleFluidStorage::isResourceBlank)
    }

    fun findExtractableStorage(variant: FluidVariant): SingleFluidStorage? {
        val extractableStorages: List<SingleFluidStorage> =
            parts.filterIndexed { slot: Int, _: SingleFluidStorage -> getStorageIO(slot).canExtract }
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

    fun sendPacket(player: ServerPlayerEntity, handler: HTFluidSyncable.Handler, vararg slotRange: Int) {
        slotRange.forEach { index: Int ->
            parts.getOrNull(index)?.let { storage: SingleFluidStorage ->
                handler.send(player, index, storage.variantStack)
            }
        }
    }

    fun sendPacket(player: ServerPlayerEntity, handler: HTFluidSyncable.Handler, slotRange: IntRange = parts.indices) {
        sendPacket(player, handler, *slotRange.toList().toIntArray())
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

    //    Builder    //

    class Builder(maxSize: Int) {
        private val slotArray: Array<HTStorageIO> = Array(maxSize) { HTStorageIO.INTERNAL }

        private fun setIO(slot: Int, storageIO: HTStorageIO) {
            check(slotArray[slot] == HTStorageIO.INTERNAL) { "Slot: $slot is already modified!" }
            slotArray[slot] = storageIO
        }

        fun input(slots: IntRange): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.INPUT) }
        }

        fun input(vararg slots: Int): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.INPUT) }
        }

        fun output(slots: IntRange): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.OUTPUT) }
        }

        fun output(vararg slots: Int): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.OUTPUT) }
        }

        fun generic(slots: IntRange): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.GENERIC) }
        }

        fun generic(vararg slots: Int): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.GENERIC) }
        }

        fun build(tier: HTMachineTier): HTMachineFluidStorage = HTMachineFluidStorage(
            slotArray.size,
            slotArray.mapIndexed { slot: Int, storageIO: HTStorageIO -> slot to storageIO }.toMap(),
            tier,
        )
    }
}
