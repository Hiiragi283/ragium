package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedSlottedStorage
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import java.util.function.BiConsumer
import java.util.function.Consumer

class HTMachineFluidStorage private constructor(parts: List<SingleFluidStorage>) :
    CombinedSlottedStorage<FluidVariant, SingleFluidStorage>(parts) {
        companion object {
            @JvmStatic
            private fun createStorage(multiplier: Int, consumer: Consumer<SingleFluidStorage>): SingleFluidStorage =
                object : SingleFluidStorage() {
                    override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * multiplier

                    override fun onFinalCommit() {
                        consumer.accept(this)
                    }
                }

            @JvmStatic
            fun of(
                sizeType: HTMachineRecipe.SizeType,
                biConsumer: BiConsumer<Int, SingleFluidStorage> = BiConsumer { _: Int, _: SingleFluidStorage -> },
            ): HTMachineFluidStorage = when (sizeType) {
                HTMachineRecipe.SizeType.SIMPLE -> listOf(
                    createStorage(16) { biConsumer.accept(0, it) },
                    createStorage(16) { biConsumer.accept(1, it) },
                )

                HTMachineRecipe.SizeType.LARGE -> listOf(
                    createStorage(64) { biConsumer.accept(0, it) },
                    createStorage(64) { biConsumer.accept(1, it) },
                    createStorage(64) { biConsumer.accept(2, it) },
                    createStorage(64) { biConsumer.accept(3, it) },
                )
            }.let(::HTMachineFluidStorage)
        }

        val sizeType: HTMachineRecipe.SizeType = when (parts.size) {
            4 -> HTMachineRecipe.SizeType.LARGE
            else -> HTMachineRecipe.SizeType.SIMPLE
        }

        operator fun get(index: Int): SingleFluidStorage = parts[index]

        // fun getOrNull(index: Int): SingleFluidStorage? = parts.getOrNull(index)

        fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            parts.forEachIndexed { index: Int, storageIn: SingleFluidStorage ->
                val nbtIn = NbtCompound()
                storageIn.writeNbt(nbtIn, wrapperLookup)
                nbt.put(index.toString(), nbtIn)
            }
        }

        fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
            parts.forEachIndexed { index: Int, storageIn: SingleFluidStorage ->
                val nbtIn: NbtCompound = nbt.getCompound(index.toString())
                storageIn.readNbt(nbtIn, wrapperLookup)
            }
        }
    }
