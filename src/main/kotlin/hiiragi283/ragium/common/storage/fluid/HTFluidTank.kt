package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.buildNbt
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

class HTFluidTank(capacity: Int, private val callback: () -> Unit) :
    FluidTank(capacity),
    INBTSerializable<CompoundTag> {
    override fun onContentsChanged() {
        callback()
    }

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        writeToNBT(provider, this)
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        readFromNBT(provider, nbt)
    }

    //    Extension    //

    fun canFill(resource: FluidStack, forceFull: Boolean): Boolean {
        val filled: Int = fill(resource, IFluidHandler.FluidAction.SIMULATE)
        return if (forceFull) filled == resource.amount else filled > 0
    }

    fun canDrain(maxDrain: Int, forceMax: Boolean): Boolean {
        val drained: FluidStack = drain(fluid.copyWithAmount(maxDrain), IFluidHandler.FluidAction.SIMULATE)
        return if (forceMax) drained.amount == maxDrain else !drained.isEmpty
    }
}
