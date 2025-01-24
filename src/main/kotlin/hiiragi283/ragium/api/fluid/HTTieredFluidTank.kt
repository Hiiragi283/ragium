package hiiragi283.ragium.api.fluid

import com.google.common.util.concurrent.Runnables
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.logError
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierUpgradable
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import org.slf4j.Logger

open class HTTieredFluidTank(override var machineTier: HTMachineTier, val callback: Runnable = Runnables.doNothing()) :
    FluidTank(machineTier.tankCapacity),
    HTMachineTierUpgradable {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

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

        @JvmStatic
        fun readFromNBT(tanks: Array<out HTTieredFluidTank>, nbt: CompoundTag, provider: HolderLookup.Provider) {
            FluidStack.OPTIONAL_CODEC
                .listOf()
                .parse(
                    provider.createSerializationContext(NbtOps.INSTANCE),
                    nbt.get("Fluids"),
                ).ifSuccess { stacks: List<FluidStack> ->
                    stacks.forEachIndexed { index: Int, stack: FluidStack -> tanks[index].fluid = stack }
                }.logError(LOGGER)
        }
    }

    constructor(machine: HTMachineBlockEntity) : this(machine.machineTier, machine::setChanged)

    override fun onContentsChanged() {
        callback.run()
    }

    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        this.machineTier = newTier
        this.capacity = newTier.tankCapacity
    }
}
