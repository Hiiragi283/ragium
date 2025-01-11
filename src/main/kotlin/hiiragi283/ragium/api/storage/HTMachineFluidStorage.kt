package hiiragi283.ragium.api.storage

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper

class HTMachineFluidStorage(private val parts: List<HTTieredFluidStorage>) :
    HTFluidInteractable,
    HTScreenFluidProvider {
    companion object {
        const val NBT_KEY = "fluid_storages"

        @JvmField
        val EMPTY = HTMachineFluidStorage(listOf())

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

    fun getStorage(index: Int): DataResult<HTTieredFluidStorage> = parts.getOrNull(index).toDataResult { "Invalid child index: $index" }

    fun <T : Any> map(index: Int, transform: (HTTieredFluidStorage) -> T?): DataResult<T> = getStorage(index).map(transform)

    fun getVariantStack(index: Int): HTFluidVariantStack = map(index, HTTieredFluidStorage::variantStack).orElse(HTFluidVariantStack.EMPTY)

    fun update(tier: HTMachineTier): HTMachineFluidStorage = parts.map { it.updateTier(tier) }.let(::HTMachineFluidStorage)

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
        parts.forEach(HTTieredFluidStorage::invokeCallback)
        nbt.put(
            NBT_KEY,
            buildNbtList {
                parts.forEach { add(buildNbt { it.writeNbt(this, wrapperLookup) }) }
            },
        )
    }

    fun wrapStorage(): CombinedStorage<FluidVariant, Storage<FluidVariant>> =
        parts.map(HTTieredFluidStorage::wrapStorage).let(::CombinedStorage)

    //    HTFluidInteractable    //

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean {
        // try to extract from outputs
        parts.forEachIndexed { index: Int, storageIn: HTTieredFluidStorage ->
            val storageIO: HTStorageIO = storageIn.storageIO
            if (!storageIO.canExtract) return@forEachIndexed
            if (player.interactWithFluidStorage(storageIn, storageIO)) {
                return true
            }
        }
        // try to extract from inputs
        parts.forEachIndexed { index: Int, storageIn: HTTieredFluidStorage ->
            if (player.interactWithFluidStorage(storageIn)) {
                return true
            }
        }
        return false
    }

    //    HTScreenFluidProvider    //

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> =
        parts.associate { storageIn: HTTieredFluidStorage -> storageIn.syncIndex to storageIn.variantStack }

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

        fun build(tier: HTMachineTier): HTMachineFluidStorage = when {
            slotArray.isEmpty() || slotArray.all { it == HTStorageIO.INTERNAL } -> EMPTY
            else ->
                slotArray
                    .mapIndexed { index: Int, storageIO: HTStorageIO ->
                        HTTieredFluidStorage(
                            tier,
                            storageIO,
                            null,
                            syncIndex = index,
                        )
                    }.let(::HTMachineFluidStorage)
        }

        fun <T> build(machine: T): HTMachineFluidStorage where T : BlockEntity, T : HTMachineTierProvider = when {
            slotArray.isEmpty() || slotArray.all { it == HTStorageIO.INTERNAL } -> EMPTY
            else ->
                slotArray
                    .mapIndexed { index: Int, storageIO: HTStorageIO ->
                        HTTieredFluidStorage(
                            machine.tier,
                            storageIO,
                            null,
                            machine::markDirty,
                            index,
                        )
                    }.let(::HTMachineFluidStorage)
        }
    }
}
