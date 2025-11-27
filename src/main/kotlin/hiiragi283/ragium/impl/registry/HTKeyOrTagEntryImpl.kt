package hiiragi283.ragium.impl.registry

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.text.HTTextResult
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.util.toTextResult
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

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
sealed class HTKeyOrTagEntryImpl<T : Any>(protected val registryKey: RegistryKey<T>) : HTKeyOrTagEntry<T> {
    companion object {
        @JvmStatic
        private val instances: MutableMap<EitherKey, HTKeyOrTagEntryImpl<*>> = hashMapOf()

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T : Any> create(key: ResourceKey<T>): HTKeyOrTagEntry<T> =
            instances.computeIfAbsent(EitherKey(key)) { KeyEntry(key) } as HTKeyOrTagEntryImpl<T>

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T : Any> create(tagKey: TagKey<T>): HTKeyOrTagEntry<T> =
            instances.computeIfAbsent(EitherKey(tagKey)) { TagEntry(tagKey) } as HTKeyOrTagEntryImpl<T>

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

    @ConsistentCopyVisibility
    @JvmRecord
    private data class EitherKey private constructor(val registryKey: RegistryKey<*>, val id: ResourceLocation, val isTag: Boolean) {
        constructor(key: ResourceKey<*>) : this(key.registryKey(), key.location(), false)

        constructor(tagKey: TagKey<*>) : this(tagKey.registry(), tagKey.location(), true)
    }

    protected var holderCache: Holder<T>? = null

    final override fun getAllHolders(provider: HolderLookup.Provider?): HTTextResult<HolderSet<T>> {
        val lookup: HolderGetter<T> = RagiumPlatform.INSTANCE.getLookup(provider, registryKey)
            ?: return HTTextResult.failure(RagiumTranslation.MISSING_REGISTRY.translate(registryKey.location()))
        return getAllHolders(lookup)
    }

    final override fun getFirstHolder(provider: HolderLookup.Provider?): HTTextResult<Holder<T>> {
        val lookup: HolderGetter<T> = RagiumPlatform.INSTANCE.getLookup(provider, registryKey)
            ?: return HTTextResult.failure(RagiumTranslation.MISSING_REGISTRY.translate(registryKey.location()))
        return getFirstHolder(lookup)
    }

    final override fun getFirstHolder(getter: HolderGetter<T>): HTTextResult<Holder<T>> {
        if (holderCache != null) {
            return HTTextResult.success(holderCache!!)
        }
        return findFirstHolder(getter)
    }

    protected abstract fun findFirstHolder(getter: HolderGetter<T>): HTTextResult<Holder<T>>

    //    KeyEntry    //

    data class KeyEntry<T : Any>(val key: ResourceKey<T>) : HTKeyOrTagEntryImpl<T>(key.registryKey()) {
        override fun findFirstHolder(getter: HolderGetter<T>): HTTextResult<Holder<T>> =
            getter.get(key).toTextResult(RagiumTranslation.MISSING_KEY, key.location())

        override fun unwrap(): Either<ResourceKey<T>, TagKey<T>> = Either.left(key)

        override fun getAllHolders(getter: HolderGetter<T>): HTTextResult<HolderSet<T>> = getFirstHolder(getter).map(HolderSet<T>::direct)

        override fun getId(): ResourceLocation = key.location()
    }

    //    TagEntry    //

    data class TagEntry<T : Any>(val tagKey: TagKey<T>) : HTKeyOrTagEntryImpl<T>(tagKey.registry()) {
        override fun findFirstHolder(getter: HolderGetter<T>): HTTextResult<Holder<T>> = getAllHolders(getter).flatMap(::getFirstHolder)

        private fun getFirstHolder(holderSet: HolderSet<T>): HTTextResult<Holder<T>> {
            for (modId: String in RagiumConfig.COMMON.tagOutputPriority.get()) {
                val foundHolder: Holder<T>? = holderSet.firstOrNull { holder: Holder<T> -> holder.idOrThrow.namespace == modId }
                if (foundHolder != null) return HTTextResult.success(foundHolder)
            }
            return holderSet.firstOrNull().toTextResult(RagiumTranslation.EMPTY_TAG_KEY, tagKey.location())
        }

        override fun unwrap(): Either<ResourceKey<T>, TagKey<T>> = Either.right(tagKey)

        override fun getAllHolders(getter: HolderGetter<T>): HTTextResult<HolderSet<T>> = getter
            .get(tagKey)
            .toTextResult(RagiumTranslation.EMPTY_TAG_KEY, tagKey.location())

        override fun getId(): ResourceLocation = tagKey.location()
    }
}
