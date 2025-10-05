package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.HTHolderLike
import net.minecraft.Util
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Function
import kotlin.streams.asSequence

//    ResourceLocation    //

fun String.toId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(this, path)

/**
 * 名前空間が`minecraft`となる[ResourceLocation]を返します。
 */

fun vanillaId(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)

/**
 * 名前空間が`c`となる[ResourceLocation]を返します。
 */
fun commonId(path: String): ResourceLocation = RagiumConst.COMMON.toId(path)

fun commonId(prefix: String, value: String): ResourceLocation = commonId("$prefix/$value")

fun ResourceKey<*>.toDescriptionKey(prefix: String, suffix: String? = null): String = location().toDescriptionKey(prefix, suffix)

fun ResourceLocation.toDescriptionKey(prefix: String, suffix: String? = null): String = buildString {
    append(Util.makeDescriptionId(prefix, this@toDescriptionKey))
    if (suffix != null) {
        append('.')
        append(suffix)
    }
}

//    Registry    //

typealias RegistryKey<T> = ResourceKey<out Registry<T>>

fun <T : Any> RegistryKey<T>.createKey(id: ResourceLocation): ResourceKey<T> = ResourceKey.create(this, id)

fun <T : Any> Registry<T>.holdersSequence(): Sequence<Holder<T>> = holders().asSequence()

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

/**
 * `block/`で前置された[HTHolderLike.getId]
 */
val HTHolderLike.blockId: ResourceLocation get() = getId().withPrefix("block/")

/**
 * `item/`で前置された[HTHolderLike.getId]
 */
val HTHolderLike.itemId: ResourceLocation get() = getId().withPrefix("item/")

//    HolderSet    //

/**
 * この[HolderSet]を[Component]に変換します。
 * @param transform 値を[Component]に変換するブロック
 * @return [TagKey]の場合は[getName]，それ以外の場合は[transform]を連結
 */
fun <T : Any> HolderSet<T>.asHolderText(transform: Function<Holder<T>, Component>): MutableComponent = unwrap()
    .map(TagKey<T>::getName) { ComponentUtils.formatList(it, transform) }
    .copy()

//    TagKey    //

fun <T : Any> RegistryKey<T>.createTagKey(id: ResourceLocation): TagKey<T> = TagKey.create(this, id)

fun <T : Any> RegistryKey<T>.createCommonTag(path: String): TagKey<T> = createTagKey(commonId(path))

fun <T : Any> RegistryKey<T>.createCommonTag(prefix: String, value: String): TagKey<T> = createTagKey(commonId(prefix, value))

/**
 * [TagKey]の名前を返します。
 */
fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
