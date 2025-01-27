package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

interface HTFluidContent :
    Supplier<Fluid>,
    StringRepresentable {
    val fluidHolder: DeferredHolder<Fluid, out Fluid>
    val typeHolder: DeferredHolder<FluidType, out FluidType>

    val commonTag: TagKey<Fluid> get() = fluidTagKey(commonId(serializedName))

    override fun get(): Fluid = fluidHolder.get()
}
