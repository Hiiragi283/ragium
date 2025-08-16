package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

open class HTFluidStackTank(capacity: Int, private val callback: () -> Unit) :
    FluidTank(capacity),
    HTFluidTank {
    override fun onContentsChanged() {
        callback()
    }

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        writeToNBT(provider, this)
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        readFromNBT(provider, nbt)
    }
}
