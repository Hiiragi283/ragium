package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.tag.getName
import hiiragi283.ragium.api.text.HTTextResult
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.util.toTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
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

fun <T : Any> Registry<T>.holdersSequence(): Sequence<Holder<T>> = holders().asSequence()

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

//    HolderGetter    //

fun <T : Any> HolderGetter<T>.getResult(key: ResourceKey<T>): HTTextResult<Holder<T>> =
    this.get(key).toTextResult(RagiumTranslation.MISSING_KEY, key.location())

fun <T : Any> HolderGetter<T>.getResult(tagKey: TagKey<T>): HTTextResult<HolderSet<T>> =
    this.get(tagKey).toTextResult(RagiumTranslation.EMPTY_TAG_KEY, tagKey.location())

//    HolderLookup    //

fun <T : Any> HolderLookup.Provider.lookupResult(key: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = TODO()

fun <T : Any> HolderLookup.Provider.holderResult(key: ResourceKey<T>): HTTextResult<Holder<T>> = TODO()

fun <T : Any> HolderLookup.Provider.holderSetResult(tagKey: TagKey<T>): HTTextResult<HolderSet<T>> = TODO()
