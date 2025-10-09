package hiiragi283.ragium.impl.registry

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.extension.createKey
import hiiragi283.ragium.api.extension.createTagKey
import hiiragi283.ragium.api.extension.flatMap
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.toResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.TagsUpdatedEvent
import java.util.Optional
import java.util.function.Function

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal data class HTKeyOrTagEntryImpl<T : Any>(
    private val registryKey: RegistryKey<T>,
    private val id: ResourceLocation,
    private val isTag: Boolean,
) : HTKeyOrTagEntry<T> {
    companion object {
        @JvmStatic
        private val instances: MutableMap<EitherKey, HTKeyOrTagEntryImpl<*>> = hashMapOf()

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T : Any> create(registryKey: RegistryKey<T>, id: ResourceLocation, isTag: Boolean): HTKeyOrTagEntryImpl<T> =
            instances.computeIfAbsent(EitherKey(registryKey, id, isTag)) { key: EitherKey ->
                val registryKey: RegistryKey<T> = key.registryKey as RegistryKey<T>
                HTKeyOrTagEntryImpl(registryKey, id, isTag)
            } as HTKeyOrTagEntryImpl<T>

        @JvmStatic
        @SubscribeEvent
        fun onTagsUpdated(event: TagsUpdatedEvent) {
            if (event.updateCause == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
                instances.values.forEach { entry: HTKeyOrTagEntryImpl<*> ->
                    entry.holderCache = null
                }
            }
            RagiumAPI.LOGGER.info("Reload Holder Cache!")
        }
    }

    override fun getId(): ResourceLocation = id

    override fun <U> map(fromKey: Function<ResourceKey<T>, U>, fromTag: Function<TagKey<T>, U>): U = when (isTag) {
        true -> fromTag.apply(registryKey.createTagKey(id))
        false -> fromKey.apply(registryKey.createKey(id))
    }

    private var holderCache: Holder<T>? = null

    override fun getFirstHolder(provider: HolderLookup.Provider?): Result<Holder<T>> = runCatching {
        RagiumPlatform.INSTANCE.getLookup(provider, registryKey) ?: error("Failed to find lookup for $registryKey")
    }.flatMap { getFirstHolder(it) }

    override fun getFirstHolder(getter: HolderGetter<T>): Result<Holder<T>> {
        if (holderCache != null) {
            return Result.success(holderCache!!)
        }
        return map(
            { key: ResourceKey<T> -> getFirstHolderFromId(getter, key) },
            { tagKey: TagKey<T> -> getFirstHolderFromTag(getter, tagKey) },
        ).onSuccess { holderCache = it }
    }

    private fun getFirstHolderFromId(lookup: HolderGetter<T>, key: ResourceKey<T>): Result<Holder<T>> =
        lookup.get(key).toResult { error("Missing key in ${key.registry()}: $key") }

    private fun getFirstHolderFromTag(lookup: HolderGetter<T>, tagKey: TagKey<T>): Result<Holder<T>> = lookup
        .get(tagKey)
        .flatMap(::getFirstHolder)
        .toResult { error("Missing tag in ${tagKey.registry().location()}: ${tagKey.location}") }

    private fun getFirstHolder(holderSet: HolderSet<T>): Optional<Holder<T>> {
        for (modId: String in RagiumConfig.COMMON.tagOutputPriority.get()) {
            val foundHolder: Optional<Holder<T>> =
                holderSet.stream().filter { holder: Holder<T> -> holder.idOrThrow.namespace == modId }.findFirst()
            if (foundHolder.isPresent) return foundHolder
        }
        return holderSet.stream().findFirst()
    }

    @JvmRecord
    private data class EitherKey(val registryKey: RegistryKey<*>, val id: ResourceLocation, val isTag: Boolean)
}
