package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedSlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper

abstract class HTMachineFluidStorage(val sizeType: HTMachineRecipe.SizeType, parts: List<SingleFluidStorage>) :
    CombinedSlottedStorage<FluidVariant, SingleFluidStorage>(parts) {
    companion object {
        @JvmStatic
        private fun createStorage(multiplier: Int): SingleFluidStorage = object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * multiplier
        }

        @JvmStatic
        fun create(sizeType: HTMachineRecipe.SizeType): HTMachineFluidStorage = when (sizeType) {
            HTMachineRecipe.SizeType.SIMPLE -> Simple()
            HTMachineRecipe.SizeType.LARGE -> Large()
        }
    }

    operator fun get(index: Int): SingleFluidStorage = parts[index]

    abstract fun createWrapped(): Storage<FluidVariant>

    abstract fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup)

    abstract fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup)

    //    Simple    //

    class Simple :
        HTMachineFluidStorage(
            HTMachineRecipe.SizeType.SIMPLE,
            listOf(
                createStorage(16),
                createStorage(16),
            ),
        ) {
        override fun createWrapped(): Storage<FluidVariant> = CombinedStorage<FluidVariant, Storage<FluidVariant>>(
            listOf(
                FilteringStorage.insertOnlyOf(get(0)),
                FilteringStorage.extractOnlyOf(get(1)),
            ),
        )

        override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            nbt.put("input", buildNbt { parts[0].writeNbt(this, wrapperLookup) })
            nbt.put("output", buildNbt { parts[1].writeNbt(this, wrapperLookup) })
        }

        override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            parts[0].readNbt(nbt.getCompound("input"), wrapperLookup)
            parts[1].readNbt(nbt.getCompound("output"), wrapperLookup)
        }
    }

    //    Large    //

    class Large :
        HTMachineFluidStorage(
            HTMachineRecipe.SizeType.SIMPLE,
            listOf(
                createStorage(64),
                createStorage(64),
                createStorage(64),
                createStorage(64),
            ),
        ) {
        override fun createWrapped(): Storage<FluidVariant> = CombinedStorage<FluidVariant, Storage<FluidVariant>>(
            listOf(
                FilteringStorage.insertOnlyOf(get(0)),
                FilteringStorage.insertOnlyOf(get(1)),
                FilteringStorage.extractOnlyOf(get(2)),
                FilteringStorage.extractOnlyOf(get(3)),
            ),
        )

        override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            nbt.put("first_input", buildNbt { parts[0].writeNbt(this, wrapperLookup) })
            nbt.put("second_output", buildNbt { parts[1].writeNbt(this, wrapperLookup) })
            nbt.put("first_output", buildNbt { parts[2].writeNbt(this, wrapperLookup) })
            nbt.put("second_output", buildNbt { parts[3].writeNbt(this, wrapperLookup) })
        }

        override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            parts[0].readNbt(nbt.getCompound("first_input"), wrapperLookup)
            parts[1].readNbt(nbt.getCompound("second_output"), wrapperLookup)
            parts[1].readNbt(nbt.getCompound("first_output"), wrapperLookup)
            parts[1].readNbt(nbt.getCompound("second_output"), wrapperLookup)
        }
    }
}
