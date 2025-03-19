package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * Ragiumで使用する[Fluid]向けの[DeferredHolder]
 */
class HTDeferredFluid<T : Fluid> private constructor(key: ResourceKey<Fluid>) : DeferredHolder<Fluid, T>(key) {
    companion object {
        @JvmStatic
        fun <T : Fluid> createFluid(key: ResourceLocation): HTDeferredFluid<T> = createFluid<T>(ResourceKey.create(Registries.FLUID, key))

        @JvmStatic
        fun <T : Fluid> createFluid(key: ResourceKey<Fluid>): HTDeferredFluid<T> = HTDeferredFluid<T>(key)
    }

    /**
     * [getId]から生成された共通タグ
     */
    @JvmField
    val commonTag: TagKey<Fluid> = fluidTagKey(commonId(id.path))

    /**
     * 指定された[amount]から[FluidStack]を返します。
     */
    fun toStack(amount: Int): FluidStack = FluidStack(get(), amount)
}
