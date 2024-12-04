package hiiragi283.ragium.api.storage

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class HTMachineFluidStorage private constructor(
    size: Int,
    private val ioMapper: (Int) -> HTStorageIO,
    private val filter: (Int, FluidVariant) -> Boolean,
    tier: HTMachineTier,
) {
    companion object {
        const val NBT_KEY = "fluid_storages"
    }

    constructor(builder: HTStorageBuilder, tier: HTMachineTier) : this(
        builder.size,
        builder.ioMapper,
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

    fun <T : Any> map(index: Int, action: (SingleFluidStorage) -> T?): DataResult<T> =
        parts.getOrNull(index)?.let(action).toDataResult { "Invalid child index: $index" }

    fun <T : Any> flatMap(index: Int, action: (SingleFluidStorage) -> DataResult<T>): DataResult<T> =
        parts.getOrNull(index)?.let(action) ?: DataResult.error { "Invalid child index: $index" }

    fun getResourceAmount(index: Int): ResourceAmount<FluidVariant> =
        map(index, SingleFluidStorage::resourceAmount).result().orElse(ResourceAmount(FluidVariant.blank(), 0))

    fun createWrapped(): Storage<FluidVariant> = CombinedStorage(
        buildList {
            parts.forEachIndexed { index: Int, storage: SingleFluidStorage ->
                add(ioMapper(index).wrapStorage(storage))
            }
        },
    )

    fun interactByPlayer(player: PlayerEntity): Boolean {
        val handStorage: Storage<FluidVariant> =
            ContainerItemContext.forPlayerInteraction(player, Hand.MAIN_HAND).find(FluidStorage.ITEM) ?: return false
        val variants: List<FluidVariant> = handStorage.nonEmptyViews().map(StorageView<FluidVariant>::getResource)
        parts.forEach {
            // prevent to insert same fluid into multiple parts
            if (it.variant in variants && it.amount == it.capacity) {
                return false
            }
            if (FluidStorageUtil.interactWithFluidStorage(it, player, Hand.MAIN_HAND)) {
                return true
            }
        }
        return false
    }

    fun update(tier: HTMachineTier): HTMachineFluidStorage = apply {
        val copied: Array<SingleFluidStorage> = parts.copyOf()
        copied.forEachIndexed { index: Int, storage: SingleFluidStorage ->
            val newStorage: SingleFluidStorage = childStorage(index, tier)
            storage.copyTo(newStorage)
            parts[index] = newStorage
        }
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
}
