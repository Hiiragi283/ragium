package hiiragi283.lib.registry

import hiiragi283.lib.resource.toId
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * [Registry]で使われる[ResourceKey]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias RegistryKey<T> = ResourceKey<out Registry<T>>

fun <T : Any> RegistryKey<T>.createKey(namespace: String, path: String): ResourceKey<T> = this.createKey(namespace.toId(path))

/**
 * この[レジストリキー][this]に基づいて[ID][id]を[ResourceKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> RegistryKey<T>.createKey(id: Identifier): ResourceKey<T> = ResourceKey.create(this, id)
