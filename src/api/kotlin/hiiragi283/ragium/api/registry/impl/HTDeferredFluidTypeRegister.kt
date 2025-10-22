package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier

class HTDeferredFluidTypeRegister(namespace: String) : HTDeferredRegister<FluidType>(NeoForgeRegistries.Keys.FLUID_TYPES, namespace) {
    fun <TYPE : FluidType> registerType(
        name: String,
        properties: FluidType.Properties,
        factory: (FluidType.Properties) -> TYPE,
    ): HTDeferredFluidType<TYPE> = register(name) { _: ResourceLocation -> factory(properties) }

    fun registerSimpleType(name: String, properties: FluidType.Properties): HTDeferredFluidType<FluidType> =
        registerType(name, properties, ::FluidType)

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredFluidType<*>> = super.asSequence().filterIsInstance<HTDeferredFluidType<*>>()

    override fun getEntries(): Collection<HTDeferredFluidType<*>> = super.getEntries().filterIsInstance<HTDeferredFluidType<*>>()

    override fun <I : FluidType> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredFluidType<I> =
        super.register(name, func) as HTDeferredFluidType<I>

    override fun <I : FluidType> register(name: String, sup: Supplier<out I>): HTDeferredFluidType<I> =
        super.register(name, sup) as HTDeferredFluidType<I>

    override fun <I : FluidType> createHolder(registryKey: RegistryKey<FluidType>, key: ResourceLocation): HTDeferredFluidType<I> =
        HTDeferredFluidType(registryKey.createKey(key))
}
