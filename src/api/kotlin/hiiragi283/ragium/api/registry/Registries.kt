package hiiragi283.ragium.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.streams.asSequence

//    RegistryKey    //

/**
 * [Registry]で使われる[ResourceKey]のエイリアス
 */
typealias RegistryKey<T> = ResourceKey<out Registry<T>>

/**
 * [RegistryKey]に基づいて[ResourceLocation]を[ResourceKey]に変換します。
 */
fun <T : Any> RegistryKey<T>.createKey(id: ResourceLocation): ResourceKey<T> = ResourceKey.create(this, id)

//    Holder    //

/**
 * [Holder]から[ResourceLocation]を返します。
 * @throws [Holder.unwrapKey]が空の場合
 */
val <T : Any> Holder<T>.idOrThrow: ResourceLocation get() = when (this) {
    is HTHolderLike -> this.getId()
    is DeferredHolder<*, *> -> this.id
    else -> unwrapKey().orElseThrow().location()
}

//    HolderLookup    //

fun <R : Any, T : Any> HolderLookup.RegistryLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<Holder.Reference<R>, T> = this
    .listElementIds()
    .asSequence()
    .mapNotNull { key: ResourceKey<R> ->
        val data: T = this.getData(type, key) ?: return@mapNotNull null
        key to data
    }.associate { (key: ResourceKey<R>, data: T) -> this.getOrThrow(key) to data }
