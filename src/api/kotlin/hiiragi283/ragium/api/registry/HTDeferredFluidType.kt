package hiiragi283.ragium.api.registry

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredFluidType<T : FluidType> private constructor(key: ResourceKey<FluidType>) : DeferredHolder<FluidType, T>(key) {
    companion object {
        @JvmStatic
        fun <T : FluidType> createType(key: ResourceLocation): HTDeferredFluidType<T> = createType<T>(
            ResourceKey.create(
                NeoForgeRegistries.Keys.FLUID_TYPES,
                key,
            ),
        )

        @JvmStatic
        fun <T : FluidType> createType(key: ResourceKey<FluidType>): HTDeferredFluidType<T> = HTDeferredFluidType<T>(key)
    }
}
