package hiiragi283.ragium.api.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier

/**
 * Ragiumで使用する[FluidType]向けの[DeferredRegister]
 */
class HTFluidTypeRegister(namespace: String) : DeferredRegister<FluidType>(NeoForgeRegistries.Keys.FLUID_TYPES, namespace) {
    /**
     * シンプルな[HTDeferredFluidType]を返します。
     * @param name [FluidType]のID
     * @param properties [FluidType]のプロパティ
     */
    fun registerSimpleType(
        name: String,
        properties: FluidType.Properties = FluidType.Properties.create(),
    ): HTDeferredFluidType<FluidType> = registerType(name, properties, ::FluidType)

    /**
     * [factory]から[HTDeferredFluidType]を返します。
     * @param I [FluidType]を継承したクラス
     * @param name [FluidType]のID
     * @param properties [FluidType]のプロパティ
     * @param factory [FluidType.Properties]から[I]を返すブロック
     */
    fun <I : FluidType> registerType(
        name: String,
        properties: FluidType.Properties = FluidType.Properties.create(),
        factory: (FluidType.Properties) -> I,
    ): HTDeferredFluidType<I> = register(name) { _: ResourceLocation -> factory(properties) }

    //    DeferredRegister    //

    override fun <I : FluidType> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredFluidType<I> =
        super.register(name, func) as HTDeferredFluidType<I>

    override fun <I : FluidType> register(name: String, sup: Supplier<out I>): HTDeferredFluidType<I> =
        super.register(name, sup) as HTDeferredFluidType<I>

    override fun <I : FluidType> createHolder(
        registryKey: ResourceKey<out Registry<FluidType>>,
        key: ResourceLocation,
    ): HTDeferredFluidType<I> = HTDeferredFluidType.createType(key)
}
