package hiiragi283.ragium.api.content

import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder

interface HTFluidContent : HTContent<Fluid> {
    val typeHolder: DeferredHolder<FluidType, out FluidType>
}
