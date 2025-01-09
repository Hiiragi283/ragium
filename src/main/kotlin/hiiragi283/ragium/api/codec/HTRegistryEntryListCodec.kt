package hiiragi283.ragium.api.codec

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import hiiragi283.ragium.api.util.HTRegistryEntryList
import hiiragi283.ragium.api.util.HTTagValueGetter
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import java.util.function.Function

/**
 * @see net.minecraft.registry.entry.RegistryEntryListCodec
 */
class HTRegistryEntryListCodec<E : Any>(
    registryKey: RegistryKey<out Registry<E>>,
    entryCodec: Codec<E>,
    private val valueGetter: HTTagValueGetter<E>,
) : Codec<HTRegistryEntryList<E>> {
    constructor(registry: Registry<E>) : this(registry.key, registry.codec, registry::iterateEntries)

    private val entryOrList: Codec<List<E>> = Codec.either(entryCodec, entryCodec.listOf()).xmap(
        { either: Either<E, List<E>> -> either.map(::listOf, Function.identity()) },
        { entries: List<E> ->
            when (entries.size) {
                1 -> Either.left(entries[0])
                else -> Either.right(entries)
            }
        },
    )

    private val entryListCodec: Codec<Either<TagKey<E>, List<E>>> = Codec.either(TagKey.codec(registryKey), entryOrList)

    override fun <T : Any> encode(input: HTRegistryEntryList<E>, ops: DynamicOps<T>, prefix: T): DataResult<T> =
        entryListCodec.encode(input.storage, ops, prefix)

    override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<HTRegistryEntryList<E>, T>> =
        entryListCodec.decode(ops, input).map { pair: Pair<Either<TagKey<E>, List<E>>, T> ->
            pair.mapFirst { either: Either<TagKey<E>, List<E>> ->
                either.map(
                    { HTRegistryEntryList.fromTag(it, valueGetter) },
                    HTRegistryEntryList.Companion::direct,
                )
            }
        }
}
