package hiiragi283.ragium.api.extension

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Texts
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

//    Registry    //

fun createWrapperLookup(): RegistryWrapper.WrapperLookup = BuiltinRegistries.createWrapperLookup()

/**
 * [net.minecraft.util.Identifier]をベースとした[Comparator]を返します。
 */
fun <T : Any> idComparator(registry: Registry<T>): Comparator<T> = compareBy(registry::getId)

/**
 * 指定した[entry]から[RegistryKey]を返します。
 * @return [entry]が紐づいていない場合はnull
 */
fun <T : Any> Registry<T>.getKeyOrNull(entry: T): RegistryKey<T>? = getKey(entry).getOrNull()

/**
 * 指定した[entry]から[RegistryKey]を返します。
 * @throws [entry]が紐づいていない場合
 */
fun <T : Any> Registry<T>.getKeyOrThrow(entry: T): RegistryKey<T> = getKey(entry).orElseThrow()

/**
 * 指定した[key]から[RegistryEntry.Reference]を返します。
 * @return [key]が紐づいていない場合はnull
 */
fun <T : Any> Registry<T>.getEntryOrNull(key: RegistryKey<T>): RegistryEntry.Reference<T>? = getEntry(key).getOrNull()

/**
 * 指定した[key]から[RegistryEntry.Reference]を返します。
 * @throws [key]が紐づいていない場合
 */
fun <T : Any> Registry<T>.getEntryOrThrow(key: RegistryKey<T>): RegistryEntry.Reference<T> = getEntry(key).orElseThrow()

//    RegistryEntry    //

val <T : Any> RegistryEntry<T>.id: Identifier?
    get() = key.getOrNull()?.value

/**
 * 指定した[value]が一致するか判定します。
 */
fun <T : Any> RegistryEntry<T>.isOf(value: T): Boolean = value() == value

//    RegistryEntryList    //

/**
 * この[RegistryEntryList]に要素が含まれているか判定します。
 */
val <T : Any> RegistryEntryList<T>.isEmpty: Boolean
    get() = size() == 0

/**
 * この[RegistryEntryList]を[Text]に変換します。
 * @param transform 値を[Text]に変換するブロック
 * @return [TagKey]の場合は[TagKey.getName]，それ以外の場合は[transform]を連結
 */
fun <T : Any> RegistryEntryList<T>.asText(transform: (T) -> Text): MutableText = storage
    .map(
        TagKey<T>::getName,
        { Texts.join(this.map(RegistryEntry<T>::value), transform) },
    ).copy()

//    TagKey    //

fun blockTagKey(id: Identifier): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id)

fun fluidTagKey(id: Identifier): TagKey<Fluid> = TagKey.of(RegistryKeys.FLUID, id)

fun itemTagKey(id: Identifier): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id)

/**
 * 指定した[TagKey]が読み込まれているかどうか判定します。
 */
fun TagKey<*>.isPopulated(): Boolean = ResourceConditions.tagsPopulated(this).test(null)

val DyeColor.tagKey: TagKey<Item>
    get() = itemTagKey(commonId("dyes/${this.asString()}"))
