package hiiragi283.lib.registry

import hiiragi283.lib.resource.toId
import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Hiiragi Seriesで使用される[DeferredRegister]の拡張クラスです。
 * @param R レジストリの要素のクラス
 * @param registryKey レジストリのキー
 * @param namespace 登録する値の名前空間
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTDeferredRegister<R : Any>(registryKey: RegistryKey<R>, namespace: String) : DeferredRegister<R>(registryKey, namespace) {
    /**
     * [名前空間][namespace]に基づいて，[パス][path]から[ID][Identifier]を作成します。
     */
    fun createId(path: String): Identifier = namespace.toId(path)

    /**
     * [名前空間][namespace]に基づいて，[パス][path]から[キー][ResourceKey]を作成します。
     */
    fun createKey(path: String): ResourceKey<R> = this.registryKey.createKey(namespace, path)

    /**
     * [ID][id]から[キー][ResourceKey]を作成します。
     */
    fun createKey(id: Identifier): ResourceKey<R> = this.registryKey.createKey(id)

    /**
     * [名前空間][namespace]に基づいて，IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][Identifier.getPath]
     * @param to 変更後のIDの[パス][Identifier.getPath]
     */
    fun addAlias(from: String, to: String) {
        this.addAlias(createId(from), createId(to))
    }

    open fun asSequence(): Sequence<HTDeferredHolder<R, *>> = this.entries.asSequence().filterIsInstance<HTDeferredHolder<R, *>>()

    override fun <I : R> createHolder(registryKey: RegistryKey<R>, key: Identifier): HTDeferredHolder<R, I> = HTDeferredHolder(registryKey, key)

    override fun <I : R> register(name: String, sup: Supplier<out I>): HTDeferredHolder<R, I> = super.register(name, sup) as HTDeferredHolder<R, I>

    override fun <I : R> register(name: String, func: Function<Identifier, out I>): HTDeferredHolder<R, I> = super.register(name, func) as HTDeferredHolder<R, I>
}
