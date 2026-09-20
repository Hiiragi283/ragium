package hiiragi283.lib.fluid

import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.client.fluid.FluidTintSource
import net.neoforged.neoforge.fluids.FluidStack

fun interface FluidStackTintSource : FluidTintSource {
    override fun color(state: FluidState): Int = -1

    abstract override fun colorAsStack(stack: FluidStack): Int
}
