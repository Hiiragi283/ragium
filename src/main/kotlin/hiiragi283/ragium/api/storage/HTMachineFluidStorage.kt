package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.extension.buildNbtList
import hiiragi283.ragium.api.extension.fluidStorageOf
import hiiragi283.ragium.api.extension.resourceAmount
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
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
import net.minecraft.util.Hand

class HTMachineFluidStorage(size: Int, private val ioMapper: (Int) -> HTStorageIO) {
    companion object {
        const val NBT_KEY = "fluid_storages"
    }

    constructor(builder: HTStorageBuilder) : this(
        builder.size,
        builder.ioMapper,
    )

    val parts: Array<SingleFluidStorage>
        get() = parts1

    private val parts1: Array<SingleFluidStorage> = Array(size) { fluidStorageOf(FluidConstants.BUCKET * 16) }

    fun get(index: Int): SingleFluidStorage = parts1[index]

    fun getResourceAmount(index: Int): ResourceAmount<FluidVariant> = get(index).resourceAmount

    fun createWrapped(): Storage<FluidVariant> = CombinedStorage(
        buildList {
            parts1.forEachIndexed { index: Int, storage: SingleFluidStorage ->
                add(ioMapper(index).wrapStorage(storage))
            }
        },
    )

    fun interactByPlayer(player: PlayerEntity): Boolean {
        val handStorage: Storage<FluidVariant> =
            ContainerItemContext.forPlayerInteraction(player, Hand.MAIN_HAND).find(FluidStorage.ITEM) ?: return false
        val variants: List<FluidVariant> = handStorage.nonEmptyViews().map(StorageView<FluidVariant>::getResource)
        parts1.forEach {
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

    fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        val list: NbtList = nbt.getList(NBT_KEY, NbtElement.COMPOUND_TYPE.toInt())
        list.forEachIndexed { index: Int, nbtElement: NbtElement ->
            if (nbtElement is NbtCompound) {
                parts1[index].readNbt(nbtElement, wrapperLookup)
            }
        }
    }

    fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        nbt.put(
            NBT_KEY,
            buildNbtList {
                parts1.forEach { add(buildNbt { it.writeNbt(this, wrapperLookup) }) }
            },
        )
    }
}
