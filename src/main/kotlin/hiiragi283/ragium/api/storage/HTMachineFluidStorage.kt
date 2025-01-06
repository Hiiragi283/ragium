package hiiragi283.ragium.api.storage

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper

class HTMachineFluidStorage(size: Int, private val slotMap: Map<Int, HTStorageIO>, tier: HTMachineTier) :
    SlottedStorage<FluidVariant>,
    HTFluidInteractable,
    HTScreenFluidProvider {
    companion object {
        const val NBT_KEY = "fluid_storages"

        @JvmStatic
        fun ofSmall(machine: HTMachineBlockEntityBase): HTMachineFluidStorage = Builder(2)
            .input(0)
            .output(1)
            .build(machine)

        @JvmStatic
        fun ofSimple(machine: HTMachineBlockEntityBase): HTMachineFluidStorage = Builder(4)
            .input(0, 1)
            .output(2, 3)
            .build(machine)

        @JvmStatic
        fun ofLarge(machine: HTMachineBlockEntityBase): HTMachineFluidStorage = Builder(10)
            .input(0, 1, 2, 3, 4)
            .output(5, 6, 7, 8, 9)
            .build(machine)
    }

    private var callback: () -> Unit = {}

    fun setCallback(callback: () -> Unit): HTMachineFluidStorage = apply {
        this.callback = callback
    }

    private val parts: Array<SingleFluidStorage> =
        Array(size) { slot: Int -> SingleFluidStorage.withFixedCapacity(tier.tankCapacity, callback) }

    fun getStorage(index: Int): DataResult<SingleFluidStorage> = parts.getOrNull(index).toDataResult { "Invalid child index: $index" }

    fun <T : Any> map(index: Int, transform: (SingleFluidStorage) -> T?): DataResult<T> = getStorage(index).map(transform)

    fun getVariantStack(index: Int): HTFluidVariantStack = map(index, SingleFluidStorage::variantStack).orElse(HTFluidVariantStack.EMPTY)

    fun update(tier: HTMachineTier): HTMachineFluidStorage = apply {
        val copied: Array<SingleFluidStorage> = parts.copyOf()
        copied.forEachIndexed { index: Int, storage: SingleFluidStorage ->
            val newStorage: SingleFluidStorage = SingleFluidStorage.withFixedCapacity(tier.tankCapacity, callback)
            newStorage.variantStack = storage.variantStack
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

    //    HTFluidInteractable    //

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean {
        // try to extract from outputs
        parts.forEachIndexed { index: Int, storageIn: SingleFluidStorage ->
            val storageIO: HTStorageIO = getStorageIO(index)
            if (!storageIO.canExtract) return@forEachIndexed
            if (player.interactWithFluidStorage(storageIn, storageIO)) {
                return true
            }
        }
        // try to extract from inputs
        parts.forEachIndexed { index: Int, storageIn: SingleFluidStorage ->
            if (player.interactWithFluidStorage(storageIn)) {
                return true
            }
        }
        return false
    }

    //    HTScreenFluidProvider    //

    fun createFluidPackets(vararg slotRange: Int): Map<Int, HTFluidVariantStack> = slotRange.associateWith { parts[it].variantStack }

    fun createFluidPackets(slotRange: IntRange): Map<Int, HTFluidVariantStack> = slotRange.associateWith { parts[it].variantStack }

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = createFluidPackets(0 until parts.size)

    //    SlottedStorage    //

    override fun insert(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long =
        findInsertableStorage(resource)?.insert(resource, maxAmount, transaction) ?: 0

    override fun extract(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long =
        findExtractableStorage(resource)?.extract(resource, maxAmount, transaction) ?: 0

    override fun iterator(): MutableIterator<StorageView<FluidVariant>> = object : AbstractList<StorageView<FluidVariant>>() {
        override val size: Int = parts.size

        override fun get(index: Int): StorageView<FluidVariant> = getStorageIO(index).wrapView(parts[index])
    }.toMutableList().iterator()

    override fun getSlotCount(): Int = parts.size

    override fun getSlot(slot: Int): SingleSlotStorage<FluidVariant> = getStorage(slot).orThrow

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

        fun <T> build(machine: T): HTMachineFluidStorage where T : BlockEntity, T : HTMachineTierProvider =
            build(machine.tier).setCallback(machine::markDirty)
    }
}
