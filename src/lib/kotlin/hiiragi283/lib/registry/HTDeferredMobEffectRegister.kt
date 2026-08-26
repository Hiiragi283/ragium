package hiiragi283.lib.registry

import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.effect.MobEffect

/**
 * [エフェクト][MobEffect]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredMobEffectRegister(namespace: String) : HTDeferredRegister<MobEffect>(Registries.MOB_EFFECT, namespace) {
    override fun asSequence(): Sequence<HTDeferredMobEffect<*>> = super.asSequence().filterIsInstance<HTDeferredMobEffect<*>>()

    override fun <I : MobEffect> createHolder(registryKey: RegistryKey<MobEffect>, key: Identifier): HTDeferredMobEffect<I> = HTDeferredMobEffect(key)

    override fun <I : MobEffect> register(name: String, sup: Supplier<out I>): HTDeferredMobEffect<I> = super.register(name, sup) as HTDeferredMobEffect<I>

    override fun <I : MobEffect> register(name: String, func: Function<Identifier, out I>): HTDeferredMobEffect<I> = super.register(name, func) as HTDeferredMobEffect<I>
}
