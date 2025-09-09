package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.extension.holdersNotEmpty
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import kotlin.sequences.filter

object HTRegistryHelper {
    @JvmStatic
    fun fluidHolderStream(): Sequence<Holder.Reference<Fluid>> = BuiltInRegistries.FLUID
        .holdersNotEmpty()
        .filter { holder: Holder.Reference<Fluid> ->
            val fluid: Fluid = holder.value()
            (fluid as? FlowingFluid)?.isSource(fluid.defaultFluidState()) ?: false
        }

    @JvmStatic
    fun fluidStream(): Sequence<Fluid> = fluidHolderStream().map(Holder.Reference<Fluid>::value)
}
