package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * 名前空間が`c`となる[ResourceLocation]を返します。
 */
fun commonId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("c", path)

//    Holder    //

/**
 * [Holder.idOrThrow]に基づいた[Comparator]を返します。
 */
fun <T : Any> createHolderSorter(): Comparator<Holder<T>> = compareBy(Holder<T>::idOrThrow)

/**
 * [Holder]から[ResourceKey]を返します。
 * @throws [Holder.unwrapKey]が空の場合
 */
val <T : Any> Holder<T>.keyOrThrow: ResourceKey<T> get() = unwrapKey().orElseThrow()

/**
 * [Holder]から[ResourceLocation]を返します。
 * @throws [Holder.unwrapKey]が空の場合
 */
val <T : Any> Holder<T>.idOrThrow: ResourceLocation get() = keyOrThrow.location()

/**
 * 指定した[value]が一致するか判定します。
 */
fun <T : Any> Holder<T>.isOf(value: T): Boolean = value() == value

/**
 * `block/`で前置された[DeferredBlock.getId]
 */
val DeferredBlock<*>.blockId: ResourceLocation get() = id.withPrefix("block/")

/**
 * 液体のIDから名前空間が`c`となる[TagKey]を返します。
 */
val DeferredHolder<Fluid, *>.commonTag: TagKey<Fluid> get() = fluidTagKey(commonId(id.path))

//    HolderSet    //

/**
 * この[HolderSet]に要素が含まれているか判定します。
 */
val <T : Any> HolderSet<T>.isEmpty: Boolean get() = size() == 0

operator fun <T : Any> HolderSet<T>.contains(value: T): Boolean = any { it.isOf(value) }

/**
 * この[HolderSet]を[Component]に変換します。
 * @param transform 値を[Component]に変換するブロック
 * @return [TagKey]の場合は[getName]，それ以外の場合は[transform]を連結
 */
fun <T : Any> HolderSet<T>.asHolderText(transform: (Holder<T>) -> Component): MutableComponent = unwrap()
    .map(TagKey<T>::getName) { ComponentUtils.formatList(it, transform) }
    .copy()

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
 * この[DyeColor]から染料のタグを返します。
 */
val DyeColor.commonTag: TagKey<Item> get() = itemTagKey(commonId("dyes/${this.serializedName}"))

/**
 * [TagKey]の名前を返します。
 */
fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
