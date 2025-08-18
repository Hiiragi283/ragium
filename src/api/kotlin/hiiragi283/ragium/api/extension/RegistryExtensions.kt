@file:Suppress("DEPRECATION")

package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.Util
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

/**
 * 名前空間が`minecraft`となる[ResourceLocation]を返します。
 */

fun vanillaId(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)

/**
 * 名前空間が`c`となる[ResourceLocation]を返します。
 */
fun commonId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(RagiumConst.COMMON, path)

fun <T : Any, I : T> DeferredRegister<T>.register(holder: DeferredHolder<T, I>, function: (ResourceLocation) -> I) {
    register(holder.id.path, function)
}

val <T : Any> DeferredRegister<T>.values: List<T> get() = entries.map(Supplier<out T>::get)

fun ResourceKey<*>.toDescriptionKey(prefix: String, suffix: String? = null): String = location().toDescriptionKey(prefix, suffix)

fun ResourceLocation.toDescriptionKey(prefix: String, suffix: String? = null): String = buildString {
    append(Util.makeDescriptionId(prefix, this@toDescriptionKey))
    if (suffix != null) {
        append('.')
        append(suffix)
    }
}

//    Holder    //

/**
 * [Holder]から[ResourceKey]を返します。
 * @throws [Holder.unwrapKey]が空の場合
 */
val <T : Any> Holder<T>.keyOrThrow: ResourceKey<T> get() = unwrapKey().orElseThrow()

/**
 * [Holder]から[ResourceLocation]を返します。
 * @throws [Holder.unwrapKey]が空の場合
 */
val <T : Any> Holder<T>.idOrThrow: ResourceLocation get() = when (this) {
    is DeferredHolder<T, *> -> this.id
    else -> keyOrThrow.location()
}

/**
 * この[ItemLike]から[Holder]を返します。
 */
fun ItemLike.asItemHolder(): Holder.Reference<Item> = asItem().builtInRegistryHolder()

/**
 * `block/`で前置された[DeferredBlock.getId]
 */
val DeferredBlock<*>.blockId: ResourceLocation get() = id.withPrefix("block/")

/**
 * `item/`で前置された[DeferredItem.getId]
 */
val DeferredItem<*>.itemId: ResourceLocation get() = id.withPrefix("item/")

//    HolderSet    //

/**
 * この[HolderSet]を[Component]に変換します。
 * @param transform 値を[Component]に変換するブロック
 * @return [TagKey]の場合は[getName]，それ以外の場合は[transform]を連結
 */
fun <T : Any> HolderSet<T>.asHolderText(transform: (Holder<T>) -> Component): MutableComponent = unwrap()
    .map(TagKey<T>::getName) { ComponentUtils.formatList(it, transform) }
    .copy()

//    HolderGetter    //

operator fun <T> HolderGetter<T>.contains(tagKey: TagKey<T>): Boolean = get(tagKey).isPresent

//    HolderLookup    //

fun HolderLookup.Provider.enchLookup(): HolderLookup.RegistryLookup<Enchantment> = lookupOrThrow(Registries.ENCHANTMENT)

//    TagKey    //

/**
 * 指定した[id]から[TagKey]を返します。
 */
fun blockTagKey(id: ResourceLocation): TagKey<Block> = TagKey.create(Registries.BLOCK, id)

/**
 * 指定した[id]から[TagKey]を返します。
 */
fun fluidTagKey(id: ResourceLocation): TagKey<Fluid> = TagKey.create(Registries.FLUID, id)

/**
 * 指定した[id]から[TagKey]を返します。
 */
fun itemTagKey(id: ResourceLocation): TagKey<Item> = TagKey.create(Registries.ITEM, id)

/**
 * [TagKey]の名前を返します。
 */
fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")

fun <T : Any> TagKey<*>.copyTo(registry: ResourceKey<out Registry<T>>): TagKey<T> = TagKey.create(registry, location)
