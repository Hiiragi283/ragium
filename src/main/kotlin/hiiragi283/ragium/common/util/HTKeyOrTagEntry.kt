package hiiragi283.ragium.common.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

class HTKeyOrTagEntry<T : Any>(val entry: Either<ResourceKey<T>, TagKey<T>>) {
    companion object {
        @JvmStatic
        fun <T : Any> codec(registryKey: ResourceKey<out Registry<T>>): BiCodec<ByteBuf, HTKeyOrTagEntry<T>> = BiCodecs
            .either(BiCodecs.resourceKey(registryKey), BiCodecs.tagKey(registryKey))
            .xmap(::HTKeyOrTagEntry, HTKeyOrTagEntry<T>::entry)
    }

    constructor(key: ResourceKey<T>) : this(Either.left(key))

    constructor(tagKey: TagKey<T>) : this(Either.right(tagKey))

    val id: ResourceLocation = entry.map(ResourceKey<T>::location, TagKey<T>::location)
    val registryKey: ResourceKey<out Registry<T>> = entry.map(ResourceKey<T>::registryKey, TagKey<T>::registry)

    fun getFirstHolder(provider: HolderLookup.Provider?): DataResult<out Holder<T>> {
        val getter: HolderGetter<T> = provider?.lookupOrThrow(registryKey)
            ?: RagiumAPI.getInstance().resolveLookup(registryKey)
            ?: return DataResult.error { "Failed to find lookup for $registryKey" }
        return getFirstHolder(getter)
    }

    fun getFirstHolder(lookup: HolderGetter<T>): DataResult<out Holder<T>> = entry.map(
        { key: ResourceKey<T> -> getFirstHolderFromId(lookup, key) },
        { tagKey: TagKey<T> -> getFirstHolderFromTag(lookup, tagKey) },
    )

    private fun getFirstHolderFromId(lookup: HolderGetter<T>, key: ResourceKey<T>): DataResult<out Holder<T>> = lookup
        .get(key)
        .map(DataResult<Holder<T>>::success)
        .orElse(DataResult.error { "Missing key in ${key.registry()}: $key" })

    private fun getFirstHolderFromTag(lookup: HolderGetter<T>, tagKey: TagKey<T>): DataResult<out Holder<T>> = lookup
        .get(tagKey)
        .flatMap(HTTagHelper::getFirstHolder)
        .map(DataResult<Holder<T>>::success)
        .orElse(DataResult.error { "Missing tag in ${tagKey.registry().location()}: ${tagKey.location}" })
}
