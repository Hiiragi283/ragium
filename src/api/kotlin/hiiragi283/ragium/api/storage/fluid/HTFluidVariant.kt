package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

data class HTFluidVariant(override val holder: Holder<Fluid>, override val components: DataComponentPatch) : HTVariant<Fluid> {
    companion object {
        @JvmField
        val EMPTY = HTFluidVariant(FluidStack.EMPTY)
    }

    constructor(stack: FluidStack) : this(stack.fluidHolder, stack.componentsPatch)

    override val isEmpty: Boolean
        get() = toStack(1).isEmpty

    fun toStack(count: Int): FluidStack = FluidStack(holder, count, components)
}
