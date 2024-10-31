package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage

sealed class HTMachineFluidStorage(val tier: HTMachineTier, val typeSize: HTMachineType.Size) {
    companion object {
        @JvmStatic
        fun fromParts(tier: HTMachineTier, parts: List<HTSingleFluidStorage>): HTMachineFluidStorage = when (parts.size) {
            2 -> Simple(tier, parts[0], parts[1])
            4 -> Large(tier, parts[0], parts[1], parts[2], parts[3])
            else -> throw IllegalStateException()
        }

        @JvmStatic
        private fun createStorage(tier: HTMachineTier): HTSingleFluidStorage = HTSingleFluidStorage(tier.tankCapacity)

        @JvmStatic
        fun create(tier: HTMachineTier, typeSize: HTMachineType.Size): HTMachineFluidStorage = when (typeSize) {
            HTMachineType.Size.SIMPLE -> Simple(tier)
            HTMachineType.Size.LARGE -> Large(tier)
        }
    }

    abstract val parts: List<HTSingleFluidStorage>

    operator fun get(index: Int): HTSingleFluidStorage = parts[index]

    abstract fun createWrapped(): Storage<FluidVariant>

    //    Simple    //

    private class Simple(
        tier: HTMachineTier,
        input: HTSingleFluidStorage = createStorage(tier),
        output: HTSingleFluidStorage = createStorage(tier),
    ) : HTMachineFluidStorage(tier, HTMachineType.Size.SIMPLE) {
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
        tier: HTMachineTier,
        firstInput: HTSingleFluidStorage = createStorage(tier),
        secondInput: HTSingleFluidStorage = createStorage(tier),
        firstOutput: HTSingleFluidStorage = createStorage(tier),
        secondOutput: HTSingleFluidStorage = createStorage(tier),
    ) : HTMachineFluidStorage(tier, HTMachineType.Size.LARGE) {
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
