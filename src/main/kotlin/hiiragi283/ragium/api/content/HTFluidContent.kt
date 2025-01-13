package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

interface HTFluidContent : HTContent<Fluid> {
    val tagKey: TagKey<Fluid>
        get() = fluidTagKey(commonId(id.path))

    override fun get(): Fluid = BuiltInRegistries.FLUID.getValueOrThrow(key)
}
