package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
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
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import kotlin.jvm.optionals.getOrNull

fun commonId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("c", path)

//    Registry    //

/**
 * [ResourceLocation]をベースとした[Comparator]を返します。
 */
fun <T : Any> idComparator(registry: Registry<T>): Comparator<T> = compareBy(registry::getId)

/**
 * 指定した[entry]から[ResourceKey]を返します。
 * @return [entry]が紐づいていない場合はnull
 */
fun <T : Any> Registry<T>.getKeyOrNull(entry: T): ResourceKey<T>? = getResourceKey(entry).getOrNull()

/**
 * 指定した[entry]から[ResourceKey]を返します。
 * @throws [entry]が紐づいていない場合
 */
fun <T : Any> Registry<T>.getKeyOrThrow(entry: T): ResourceKey<T> = getResourceKey(entry).orElseThrow()

/**
 * 指定した[key]から[Holder.Reference]を返します。
 * @return [key]が紐づいていない場合はnull
 */
fun <T : Any> Registry<T>.getEntryOrNull(key: ResourceKey<T>): Holder.Reference<T>? = getHolder(key).getOrNull()

/**
 * 指定した[key]から[Holder.Reference]を返します。
 * @throws [key]が紐づいていない場合
 */
fun <T : Any> Registry<T>.getEntryOrThrow(key: ResourceKey<T>): Holder.Reference<T> = getHolder(key).orElseThrow()

//    ResourceKey    //

fun <T : Any> ResourceKey<T>.withPrefix(prefix: String): ResourceKey<T> =
    ResourceKey.create(this.registryKey(), this.location().withPrefix(prefix))

fun <T : Any> ResourceKey<T>.withSuffix(suffix: String): ResourceKey<T> =
    ResourceKey.create(this.registryKey(), this.location().withSuffix(suffix))

//    Holder    //

val <T : Any> Holder<T>.id: ResourceLocation?
    get() = key?.location()

/**
 * 指定した[value]が一致するか判定します。
 */
fun <T : Any> Holder<T>.isOf(value: T): Boolean = value() == value

//    HolderSet    //

/**
 * この[HolderSet]に要素が含まれているか判定します。
 */
val <T : Any> HolderSet<T>.isEmpty: Boolean
    get() = size() == 0

/**
 * この[HolderSet]を[Component]に変換します。
 * @param transform 値を[Component]に変換するブロック
 * @return [TagKey]の場合は[TODO]，それ以外の場合は[transform]を連結
 */
fun <T : Any> HolderSet<T>.asText(transform: (T) -> Component): MutableComponent = unwrap()
    .map(
        TODO(),
        { ComponentUtils.formatList(this.map(Holder<T>::value), transform) },
    ).copy()

//    TagKey    //

fun blockTagKey(id: ResourceLocation): TagKey<Block> = TagKey.create(Registries.BLOCK, id)

fun fluidTagKey(id: ResourceLocation): TagKey<Fluid> = TagKey.create(Registries.FLUID, id)

fun itemTagKey(id: ResourceLocation): TagKey<Item> = TagKey.create(Registries.ITEM, id)

fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
