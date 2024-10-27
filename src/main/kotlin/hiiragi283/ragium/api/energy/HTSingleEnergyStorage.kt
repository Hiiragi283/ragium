package hiiragi283.ragium.api.energy

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.buildNbt
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper

abstract class HTSingleEnergyStorage : SingleVariantStorage<HTEnergyType>() {
    companion object {
        @JvmStatic
        fun of(capacity: Long): HTSingleEnergyStorage = object : HTSingleEnergyStorage() {
            override fun getCapacity(variant: HTEnergyType): Long = capacity
        }

        @JvmStatic
        fun ofFiltered(capacity: Long, type: HTEnergyType): HTSingleEnergyStorage = object : HTSingleEnergyStorage() {
            override fun getCapacity(variant: HTEnergyType): Long = when {
                variant.isOf(type) -> capacity
                else -> 0
            }

            override fun canInsert(variant: HTEnergyType): Boolean = variant.isOf(type)

            override fun canExtract(variant: HTEnergyType): Boolean = variant.isOf(type)
        }

        @JvmStatic
        fun createCodec(capacity: Long, wrapperLookup: RegistryWrapper.WrapperLookup): Codec<HTSingleEnergyStorage> =
            NbtCompound.CODEC.xmap(
                { nbt: NbtCompound -> of(capacity).apply { readNbt(nbt, wrapperLookup) } },
                { storage: HTSingleEnergyStorage -> buildNbt { storage.writeNbt(this, wrapperLookup) } },
            )
    }

    final override fun getBlankVariant(): HTEnergyType = HTEnergyType.EMPTY

    fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        readNbt(this, HTEnergyType.CODEC, HTEnergyType.Companion::EMPTY, nbt, wrapperLookup)
    }

    fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        writeNbt(this, HTEnergyType.CODEC, nbt, wrapperLookup)
    }
}
