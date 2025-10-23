package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.function.IdToFunction
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class HTDeferredFluidRegister(namespace: String) : HTDeferredRegister<Fluid>(Registries.FLUID, namespace) {
    fun <FLUID : BaseFlowingFluid> registerFluid(
        name: String,
        properties: BaseFlowingFluid.Properties,
        factory: Function<BaseFlowingFluid.Properties, FLUID>,
    ): HTDeferredFluid<FLUID> = register(name) { _: ResourceLocation -> factory.apply(properties) }

    fun registerSource(name: String, properties: BaseFlowingFluid.Properties): HTDeferredFluid<BaseFlowingFluid.Source> =
        registerFluid(name, properties, BaseFlowingFluid::Source)

    fun registerFlowing(name: String, properties: BaseFlowingFluid.Properties): HTDeferredFluid<BaseFlowingFluid.Flowing> =
        registerFluid(name, properties, BaseFlowingFluid::Flowing)

    fun registerFluids(
        name: String,
        type: Supplier<out FluidType>,
        operator: UnaryOperator<BaseFlowingFluid.Properties>,
    ): Pair<HTDeferredFluid<BaseFlowingFluid.Source>, HTDeferredFluid<BaseFlowingFluid.Flowing>> {
        val still: HTDeferredFluid<BaseFlowingFluid.Source> = HTDeferredFluid(createId(name))
        val flowing: HTDeferredFluid<BaseFlowingFluid.Flowing> = HTDeferredFluid(createId("flowing_$name"))
        val properties: BaseFlowingFluid.Properties = BaseFlowingFluid.Properties(type, still, flowing).let(operator::apply)
        return registerSource(still.id.path, properties) to registerFlowing(flowing.id.path, properties)
    }

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredFluid<*>> = super.asSequence().filterIsInstance<HTDeferredFluid<*>>()

    override fun getEntries(): Collection<HTDeferredFluid<*>> = super.getEntries().filterIsInstance<HTDeferredFluid<*>>()

    override fun <I : Fluid> register(name: String, func: IdToFunction<out I>): HTDeferredFluid<I> =
        super.register(name, func) as HTDeferredFluid<I>

    override fun <I : Fluid> register(name: String, sup: Supplier<out I>): HTDeferredFluid<I> =
        super.register(name, sup) as HTDeferredFluid<I>

    override fun <I : Fluid> createHolder(registryKey: RegistryKey<Fluid>, key: ResourceLocation): HTDeferredFluid<I> =
        HTDeferredFluid(registryKey.createKey(key))
}
