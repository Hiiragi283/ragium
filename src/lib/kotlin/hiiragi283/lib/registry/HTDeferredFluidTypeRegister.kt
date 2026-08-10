package hiiragi283.lib.registry

import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * [FluidType]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredFluidTypeRegister(namespace: String) : HTDeferredRegister<FluidType>(NeoForgeRegistries.Keys.FLUID_TYPES, namespace) {
    /**
     * 新しい[FluidType]を登録します。
     * @param name [FluidType]のIDのパス
     * @param properties [FluidType]のプロパティ
     * @return 新しい[HTDeferredFluidType]のインスタンス
     */
    fun registerSimpleType(name: String, properties: FluidType.Properties): HTDeferredFluidType<FluidType> = registerType(name, properties, ::FluidType)

    /**
     * 新しい[FluidType]を登録します。
     * @param TYPE [FluidType]のクラス
     * @param name [FluidType]のIDのパス
     * @param properties [FluidType]のプロパティ
     * @param factory [FluidType.Properties]から[FluidType]を作るブロック
     * @return 新しい[HTDeferredFluidType]のインスタンス
     */
    fun <TYPE : FluidType> registerType(name: String, properties: FluidType.Properties, factory: (FluidType.Properties) -> TYPE): HTDeferredFluidType<TYPE> = this.register(name) { _ -> properties.let(factory) }

    //    HTDeferredRegister    //

    override fun <I : FluidType> createHolder(registryKey: RegistryKey<FluidType>, key: Identifier): HTDeferredFluidType<I> = HTDeferredFluidType(key)

    override fun <I : FluidType> register(name: String, sup: Supplier<out I>): HTDeferredFluidType<I> = super.register(name, sup) as HTDeferredFluidType<I>

    override fun <I : FluidType> register(name: String, func: Function<Identifier, out I>): HTDeferredFluidType<I> = super.register(name, func) as HTDeferredFluidType<I>

    override fun asSequence(): Sequence<HTDeferredFluidType<*>> = super.asSequence().filterIsInstance<HTDeferredFluidType<*>>()
}
