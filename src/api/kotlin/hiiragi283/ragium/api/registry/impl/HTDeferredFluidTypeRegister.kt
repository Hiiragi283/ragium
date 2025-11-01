package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.function.IdToFunction
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class HTDeferredFluidTypeRegister(namespace: String) : HTDeferredRegister<FluidType>(NeoForgeRegistries.Keys.FLUID_TYPES, namespace) {
    fun <TYPE : FluidType> registerType(
        name: String,
        properties: FluidType.Properties,
        factory: Function<FluidType.Properties, TYPE>,
    ): HTDeferredFluidType<TYPE> = register(name) { _: ResourceLocation -> factory.apply(properties) }

    fun <TYPE : FluidType> registerType(
        name: String,
        factory: Function<FluidType.Properties, TYPE>,
        operator: UnaryOperator<FluidType.Properties>,
    ): HTDeferredFluidType<TYPE> = register(name) { _: ResourceLocation -> factory.apply(operator.apply(FluidType.Properties.create())) }

    fun registerSimpleType(name: String, operator: UnaryOperator<FluidType.Properties>): HTDeferredFluidType<FluidType> =
        registerType(name, ::FluidType, operator)

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredFluidType<*>> = super.asSequence().filterIsInstance<HTDeferredFluidType<*>>()

    override fun getEntries(): Collection<HTDeferredFluidType<*>> = super.getEntries().filterIsInstance<HTDeferredFluidType<*>>()

    override fun <I : FluidType> register(name: String, func: IdToFunction<out I>): HTDeferredFluidType<I> =
        super.register(name, func) as HTDeferredFluidType<I>

    override fun <I : FluidType> register(name: String, sup: Supplier<out I>): HTDeferredFluidType<I> =
        super.register(name, sup) as HTDeferredFluidType<I>

    override fun <I : FluidType> createHolder(registryKey: RegistryKey<FluidType>, key: ResourceLocation): HTDeferredFluidType<I> =
        HTDeferredFluidType(registryKey.createKey(key))
}
