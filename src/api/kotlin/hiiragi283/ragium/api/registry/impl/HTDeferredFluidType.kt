package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredFluidType<TYPE : FluidType> :
    HTDeferredHolder<FluidType, TYPE>,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<FluidType>) : super(key)

    constructor(id: ResourceLocation) : super(NeoForgeRegistries.Keys.FLUID_TYPES, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().description
}
