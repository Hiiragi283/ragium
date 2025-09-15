package hiiragi283.ragium.common.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.extension.createKey
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.lookupOrNull
import hiiragi283.ragium.api.extension.mapNotNull
import hiiragi283.ragium.api.extension.wrapDataResult
import hiiragi283.ragium.config.RagiumConfig
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import java.util.function.Function

data class HTKeyOrTagEntry<T : Any>(val entry: Either<ResourceKey<T>, TagKey<T>>) {
    companion object {
        @JvmStatic
        fun <T : Any> codec(registryKey: RegistryKey<T>): BiCodec<ByteBuf, HTKeyOrTagEntry<T>> = BiCodecs
            .either(BiCodecs.resourceKey(registryKey), BiCodecs.tagKey(registryKey))
            .xmap(::HTKeyOrTagEntry, HTKeyOrTagEntry<T>::entry)
    }

    constructor(registryKey: RegistryKey<T>, id: ResourceLocation) : this(registryKey.createKey(id))

    constructor(key: ResourceKey<T>) : this(Either.left(key))

    constructor(tagKey: TagKey<T>) : this(Either.right(tagKey))

    val id: ResourceLocation = map(ResourceKey<T>::location, TagKey<T>::location)
    val registryKey: RegistryKey<T> = map(ResourceKey<T>::registryKey, TagKey<T>::registry)

    fun <U> map(fromKey: Function<ResourceKey<T>, U>, fromTag: Function<TagKey<T>, U>): U = entry.map(fromKey, fromTag)

    fun getFirstHolder(provider: HolderLookup.Provider?): DataResult<out Holder<T>> {
        val getter: HolderGetter<T> = provider?.lookupOrNull(registryKey)
            ?: RagiumAPI.INSTANCE.getLookup(registryKey)
            ?: return DataResult.error { "Failed to find lookup for $registryKey" }
        return getFirstHolder(getter)
    }

    fun getFirstHolder(lookup: HolderGetter<T>): DataResult<out Holder<T>> = map(
        { key: ResourceKey<T> -> getFirstHolderFromId(lookup, key) },
        { tagKey: TagKey<T> -> getFirstHolderFromTag(lookup, tagKey) },
    )

    private fun getFirstHolderFromId(lookup: HolderGetter<T>, key: ResourceKey<T>): DataResult<out Holder<T>> =
        lookup.get(key).wrapDataResult("Missing key in ${key.registry()}: $key")

    private fun getFirstHolderFromTag(lookup: HolderGetter<T>, tagKey: TagKey<T>): DataResult<out Holder<T>> = lookup
        .get(tagKey)
        .mapNotNull(::getFirstHolder)
        .wrapDataResult("Missing tag in ${tagKey.registry().location()}: ${tagKey.location}")

    private fun getFirstHolder(holderSet: HolderSet<T>): Holder<T>? {
        for (modId: String in RagiumConfig.COMMON.tagOutputPriority.get()) {
            val foundHolder: Holder<T>? = holderSet.firstOrNull { holder: Holder<T> -> holder.idOrThrow.namespace == modId }
            if (foundHolder != null) return foundHolder
        }
        return holderSet.firstOrNull()
    }
}
