package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.tag.getName
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.datamaps.DataMapType
import java.util.function.Function
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

//    Registry    //

fun <R : Any, T : Any> Registry<R>.getHolderDataMap(type: DataMapType<R, T>): Map<Holder.Reference<R>, T> =
    this.getDataMap(type).mapKeys { (key: ResourceKey<R>, _) -> this.getHolderOrThrow(key) }

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

//    HolderSet    //

/**
 * この[HolderSet]を[Component]に変換します。
 * @param transform 値を[Component]に変換するブロック
 * @return [TagKey]の場合は[getName]，それ以外の場合は[transform]を連結
 */
fun <T : Any> HolderSet<T>.asHolderText(transform: Function<Holder<T>, Component>): MutableComponent = unwrap()
    .map(TagKey<T>::getName) { ComponentUtils.formatList(it, transform) }
    .copy()

//    HolderLookup    //

fun <R : Any, T : Any> HolderLookup.RegistryLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<Holder.Reference<R>, T> = this
    .listElementIds()
    .asSequence()
    .mapNotNull { key: ResourceKey<R> ->
        val data: T = this.getData(type, key) ?: return@mapNotNull null
        key to data
    }.associate { (key: ResourceKey<R>, data: T) -> this.getOrThrow(key) to data }
