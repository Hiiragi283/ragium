package hiiragi283.ragium.api.fluid

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage

sealed class HTMachineFluidStorage(val typeSize: HTMachineType.Size) {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineFluidStorage> =
            HTSingleFluidStorage.CODEC.listOf().xmap(::fromParts, HTMachineFluidStorage::parts)

        @JvmStatic
        fun fromParts(parts: List<HTSingleFluidStorage>): HTMachineFluidStorage = when (parts.size) {
            2 -> Simple(parts[0], parts[1])
            4 -> Large(parts[0], parts[1], parts[2], parts[3])
            else -> throw IllegalStateException()
        }

        @JvmStatic
        private fun createStorage(multiplier: Int): HTSingleFluidStorage = HTSingleFluidStorage(FluidConstants.BUCKET * multiplier)

        @JvmStatic
        fun create(typeSize: HTMachineType.Size): HTMachineFluidStorage = when (typeSize) {
            HTMachineType.Size.SIMPLE -> Simple()
            HTMachineType.Size.LARGE -> Large()
        }
    }

    abstract val parts: List<HTSingleFluidStorage>

    operator fun get(index: Int): HTSingleFluidStorage = parts[index]

    abstract fun createWrapped(): Storage<FluidVariant>

    //    Simple    //

    private class Simple(input: HTSingleFluidStorage = createStorage(16), output: HTSingleFluidStorage = createStorage(16)) :
        HTMachineFluidStorage(HTMachineType.Size.SIMPLE) {
        override val parts: List<HTSingleFluidStorage> = listOf(input, output)

        override fun createWrapped(): Storage<FluidVariant> = CombinedStorage<FluidVariant, Storage<FluidVariant>>(
            listOf(
                FilteringStorage.insertOnlyOf(get(0)),
                FilteringStorage.extractOnlyOf(get(1)),
            ),
        )
    }

    //    Large    //

    private class Large(
        firstInput: HTSingleFluidStorage = createStorage(64),
        secondInput: HTSingleFluidStorage = createStorage(64),
        firstOutput: HTSingleFluidStorage = createStorage(64),
        secondOutput: HTSingleFluidStorage = createStorage(64),
    ) : HTMachineFluidStorage(HTMachineType.Size.LARGE) {
        override val parts: List<HTSingleFluidStorage> = listOf(
            firstInput,
            secondInput,
            firstOutput,
            secondOutput,
        )

        override fun createWrapped(): Storage<FluidVariant> = CombinedStorage<FluidVariant, Storage<FluidVariant>>(
            listOf(
                FilteringStorage.insertOnlyOf(get(0)),
                FilteringStorage.insertOnlyOf(get(1)),
                FilteringStorage.extractOnlyOf(get(2)),
                FilteringStorage.extractOnlyOf(get(3)),
            ),
        )
    }
}
