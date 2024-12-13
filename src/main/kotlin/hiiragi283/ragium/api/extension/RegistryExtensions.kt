package hiiragi283.ragium.api.extension

import net.minecraft.registry.BuiltinRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Texts
import kotlin.jvm.optionals.getOrNull

//    Registry    //

fun createWrapperLookup(): RegistryWrapper.WrapperLookup = BuiltinRegistries.createWrapperLookup()

fun <T : Any> idComparator(registry: Registry<T>): java.util.Comparator<T> = compareBy(registry::getId)

fun <T : Any> entryComparator(registry: Registry<T>): Comparator<RegistryEntry<T>> = compareBy { it.key.orElseThrow().value }

fun <T : Any> Registry<T>.getKeyOrNull(entry: T): RegistryKey<T>? = getKey(entry).getOrNull()

fun <T : Any> Registry<T>.getKeyOrThrow(entry: T): RegistryKey<T> = getKey(entry).orElseThrow()

fun <T : Any> Registry<T>.getEntryOrNull(key: RegistryKey<T>): RegistryEntry.Reference<T>? = getEntry(key).getOrNull()

fun <T : Any> Registry<T>.getEntryOrThrow(key: RegistryKey<T>): RegistryEntry.Reference<T> = getEntry(key).orElseThrow()

fun <T : Any> Registry<T>.getEntryListOrEmpty(tagKey: TagKey<T>): RegistryEntryList<T> =
    getEntryList(tagKey).map { it as RegistryEntryList<T> }.orElse(RegistryEntryList.empty())

fun <T : Any> Registry<T>.getEntryListOrNull(tagKey: TagKey<T>): RegistryEntryList.Named<T>? = getEntryList(tagKey).getOrNull()

fun <T : Any> Registry<T>.getEntryListOrThrow(tagKey: TagKey<T>): RegistryEntryList.Named<T> = getEntryList(tagKey).orElseThrow()

//    RegistryEntry    //

fun <T : Any> RegistryEntry<T>.isOf(value: T): Boolean = value() == value

//    RegistryEntryList    //

val <T : Any> RegistryEntryList<T>.isEmpty: Boolean
    get() = size() == 0

/**
 * Transform [this] registry entry list into [MutableText] by [TagKey.getName] or [transform]
 * @param transform used when [RegistryEntryList.getStorage] returns a list of [RegistryEntry]
 */
fun <T : Any> RegistryEntryList<T>.asText(transform: (T) -> Text): MutableText = storage
    .map(
        TagKey<T>::getName,
        { Texts.join(this.map(RegistryEntry<T>::value), transform) },
    ).copy()
