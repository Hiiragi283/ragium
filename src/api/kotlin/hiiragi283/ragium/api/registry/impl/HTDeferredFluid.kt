package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid

class HTDeferredFluid<FLUID : Fluid> : HTDeferredHolder<Fluid, FLUID> {
    constructor(key: ResourceKey<Fluid>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.FLUID, id)
}
