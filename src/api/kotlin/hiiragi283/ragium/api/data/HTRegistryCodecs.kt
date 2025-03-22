package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.registry.HTDeferredFluid
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

object HTRegistryCodecs {
    @JvmField
    val ITEM_HOLDER: Codec<Holder<Item>> = Codec.withAlternative(
        ItemStack.ITEM_NON_AIR_CODEC,
        ResourceLocation.CODEC.xmap(
            { id: ResourceLocation -> DeferredItem.createItem<Item>(id) },
            DeferredItem<*>::getId,
        ),
    )

    @JvmField
    val FLUID_HOLDER: Codec<Holder<Fluid>> = Codec.withAlternative(
        FluidStack.FLUID_NON_EMPTY_CODEC,
        ResourceLocation.CODEC.xmap(
            { id: ResourceLocation -> HTDeferredFluid.createFluid<Fluid>(id) },
            HTDeferredFluid<*>::getId,
        ),
    )

    @JvmStatic
    fun <T : Any> deferredHolder(registry: ResourceKey<out Registry<T>>): Codec<DeferredHolder<T, T>> = ResourceLocation.CODEC.xmap(
        { id: ResourceLocation -> DeferredHolder.create(registry, id) },
        DeferredHolder<T, T>::getId,
    )

    @JvmStatic
    fun <T : Any> holder(registry: ResourceKey<out Registry<T>>): Codec<Holder<T>> =
        Codec.withAlternative(RegistryFixedCodec.create(registry), deferredHolder(registry))
}
