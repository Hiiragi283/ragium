package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.fluid.Fluid
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey

interface HTFluidContent : HTContent<Fluid> {
    val tagKey: TagKey<Fluid>
        get() = fluidTagKey(commonId(id.path))

    val variant: FluidVariant
        get() = FluidVariant.of(get())

    override fun get(): Fluid = Registries.FLUID.getOrThrow(key)
}
