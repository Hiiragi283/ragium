package hiiragi283.ragium.api.fluid

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.logError
import hiiragi283.ragium.api.extension.runNothing
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierUpgradable
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import org.slf4j.Logger

/**
 * [HTMachineTier]に容量を依存した[FluidTank]
 * @param callback [FluidTank.onContentsChanged]で呼び出されるブロック
 */
open class HTTieredFluidTank(override var machineTier: HTMachineTier, val callback: () -> Unit = runNothing()) :
    FluidTank(machineTier.tankCapacity),
    HTMachineTierUpgradable {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        /**
         * 指定した[tanks]の値を[nbt]に書き込みます。
         */
        @JvmStatic
        fun writeToNBT(tanks: Array<out HTTieredFluidTank>, nbt: CompoundTag, provider: HolderLookup.Provider) {
            FluidStack.OPTIONAL_CODEC
                .listOf()
                .encodeStart(
                    provider.createSerializationContext(NbtOps.INSTANCE),
                    tanks.map(HTTieredFluidTank::getFluid),
                ).ifSuccess { result: Tag -> nbt.put("Fluids", result) }
                .logError(LOGGER)
        }

        /**
         * 指定した[tanks]の容量を[newTier]で更新しつつ，[nbt]から読み取ります。
         */
        @JvmStatic
        fun readFromNBT(
            tanks: Array<out HTTieredFluidTank>,
            nbt: CompoundTag,
            provider: HolderLookup.Provider,
            newTier: HTMachineTier,
        ) {
            FluidStack.OPTIONAL_CODEC
                .listOf()
                .parse(
                    provider.createSerializationContext(NbtOps.INSTANCE),
                    nbt.get("Fluids"),
                ).ifSuccess { stacks: List<FluidStack> ->
                    stacks.forEachIndexed { index: Int, stack: FluidStack ->
                        val tank: HTTieredFluidTank = tanks[index]
                        tank.onUpdateTier(HTMachineTier.BASIC, newTier)
                        tanks[index].fluid = stack
                    }
                }.logError(LOGGER)
        }
    }

    constructor(machine: HTMachineBlockEntity) : this(machine.machineTier, machine::setChanged)

    override fun onContentsChanged() {
        callback()
    }

    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        this.machineTier = newTier
        this.capacity = newTier.tankCapacity
    }
}
