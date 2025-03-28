package hiiragi283.ragium.api.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

/**
 * Ragiumで使用する[Fluid]向けの[DeferredRegister]
 */
class HTFluidRegister(namespace: String) : DeferredRegister<Fluid>(Registries.FLUID, namespace) {
    //    DeferredRegister    //

    override fun getEntries(): List<HTDeferredFluid<*>> = super.getEntries().map { holder: DeferredHolder<Fluid, *> ->
        HTDeferredFluid.createFluid<Fluid>(holder.id)
    }

    override fun <I : Fluid> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredFluid<I> =
        super.register(name, func) as HTDeferredFluid<I>

    override fun <I : Fluid> register(name: String, sup: Supplier<out I>): HTDeferredFluid<I> =
        super.register(name, sup) as HTDeferredFluid<I>

    override fun <I : Fluid> createHolder(registryKey: ResourceKey<out Registry<Fluid>>, key: ResourceLocation): HTDeferredFluid<I> =
        HTDeferredFluid.createFluid(key)
}
