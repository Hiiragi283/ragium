package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import java.util.function.Predicate

open class HTFluidStackTank(capacity: Int, private val callback: HTContentListener) :
    FluidTank(capacity),
    HTFluidTank {
    override fun onContentsChanged() {
        callback.onContentsChanged()
    }

    fun setValidator(content: HTFluidContent<*, *, *>): HTFluidStackTank = setValidator(content.commonTag)

    fun setValidator(tagKey: TagKey<Fluid>): HTFluidStackTank = setValidator { stack: FluidStack -> stack.`is`(tagKey) }

    override fun setValidator(validator: Predicate<FluidStack>): HTFluidStackTank = apply {
        super.setValidator(validator)
    }

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        writeToNBT(provider, this)
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        readFromNBT(provider, nbt)
    }
}
