@file:Suppress("DEPRECATION")

package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
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
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import kotlin.jvm.optionals.getOrNull

/**
 * 名前空間が`minecraft`となる[ResourceLocation]を返します。
 */

fun vanillaId(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)

/**
 * 名前空間が`c`となる[ResourceLocation]を返します。
 */
fun commonId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("c", path)

fun commonId(prefix: String, value: String): ResourceLocation = commonId("$prefix/$value")

//    Holder    //

/**
 * [Holder]から[ResourceLocation]を返します。
 * @throws [Holder.unwrapKey]が空の場合
 */
val <T : Any> Holder<T>.idOrThrow: ResourceLocation get() = unwrapKey().orElseThrow().location()

val <T : Any> Holder<T>.idOrNull: ResourceLocation? get() = unwrapKey().map(ResourceKey<T>::location).getOrNull()

/**
 * 指定した[value]が一致するか判定します。
 */
fun <T : Any> Holder<T>.isOf(value: T): Boolean = value() == value

fun Fluid.asFluidHolder(): Holder.Reference<Fluid> = builtInRegistryHolder()

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

fun blockHolderSet(vararg blocks: Block): HolderSet.Direct<Block> = HolderSet.direct(Block::builtInRegistryHolder, *blocks)

fun blockHolderSet(blocks: Collection<Block>): HolderSet.Direct<Block> = HolderSet.direct(Block::builtInRegistryHolder, blocks)

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

fun HolderLookup.Provider.blockLookup(): HolderLookup.RegistryLookup<Block> = lookupOrThrow(Registries.BLOCK)

fun HolderLookup.Provider.fluidLookup(): HolderLookup.RegistryLookup<Fluid> = lookupOrThrow(Registries.FLUID)

fun HolderLookup.Provider.itemLookup(): HolderLookup.RegistryLookup<Item> = lookupOrThrow(Registries.ITEM)

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
 * この[DyeColor]から染料のタグを返します。
 */
val DyeColor.commonTag: TagKey<Item> get() = itemTagKey(commonId("dyes/${this.serializedName}"))

/**
 * [TagKey]の名前を返します。
 */
fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
