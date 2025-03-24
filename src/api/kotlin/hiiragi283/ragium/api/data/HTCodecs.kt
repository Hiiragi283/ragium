package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.registry.HTDeferredFluid
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

object HTCodecs {
    @JvmField
    val BLOCK_HOLDER: Codec<DeferredBlock<*>> = ResourceLocation.CODEC.xmap(
        { id: ResourceLocation -> DeferredBlock.createBlock<Block>(id) },
        DeferredBlock<*>::getId,
    )

    @JvmField
    val FLUID_HOLDER: Codec<HTDeferredFluid<*>> = ResourceLocation.CODEC.xmap(
        { id: ResourceLocation -> HTDeferredFluid.createFluid<Fluid>(id) },
        HTDeferredFluid<*>::getId,
    )

    @JvmField
    val ITEM_HOLDER: Codec<DeferredItem<*>> = ResourceLocation.CODEC.xmap(
        { id: ResourceLocation -> DeferredItem.createItem<Item>(id) },
        DeferredItem<*>::getId,
    )

    @JvmField
    val INT_RANGE: Codec<IntRange> = RecordCodecBuilder.create { instance ->
        instance
            .group(
                Codec.INT.fieldOf("min").forGetter(IntRange::first),
                Codec.INT.fieldOf("max").forGetter(IntRange::last),
            ).apply(instance) { min: Int, max: Int -> (min..max) }
    }

    @JvmStatic
    fun <T : Any> deferredHolder(registry: ResourceKey<out Registry<T>>): Codec<DeferredHolder<T, T>> = ResourceLocation.CODEC.xmap(
        { id: ResourceLocation -> DeferredHolder.create(registry, id) },
        DeferredHolder<T, T>::getId,
    )

    @JvmStatic
    fun <T : Any> holder(registry: ResourceKey<out Registry<T>>): Codec<Holder<T>> =
        Codec.withAlternative(RegistryFixedCodec.create(registry), deferredHolder(registry))
}
