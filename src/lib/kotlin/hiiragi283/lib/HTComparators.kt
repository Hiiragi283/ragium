package hiiragi283.lib

import com.google.common.collect.Comparators
import net.minecraft.core.HolderSet
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.holdersets.CompositeHolderSet
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

/**
 * Hiiragi Seriesで使用される[Comparator]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTComparators {
    /**
     * [ID][Identifier]の[Comparator]
     */
    @JvmField
    val ID: Comparator<Identifier> = compareBy(Identifier::getNamespace).thenComparing(Identifier::getPath)

    /**
     * [ResourceKey]の[Comparator]
     */
    @JvmField
    val KEY: Comparator<ResourceKey<*>> =
        compareBy(ID, ResourceKey<*>::registry).thenComparing(compareBy(ID, ResourceKey<*>::identifier))

    /**
     * [TagKey]の[Comparator]
     */
    @JvmField
    val TAG_KEY: Comparator<TagKey<*>> =
        compareBy(KEY, TagKey<*>::registry).thenComparing(compareBy(ID, TagKey<*>::location))

    /**
     * [HolderSet]の[Comparator]
     * @since 26.1.7
     */
    @JvmField
    val HOLDER_SET: Comparator<HolderSet<*>> = compareBy(Comparators.emptiesLast(TAG_KEY), HolderSet<*>::unwrapKey)

    /**
     * @since 26.1.7
     */
    @JvmStatic
    fun <T : Any> compressHolderSet(holderSets: Iterable<HolderSet<T>>): OrHolderSet<T> =
        holderSets.flatMap { holderSet: HolderSet<T> ->
            when (holderSet) {
                is CompositeHolderSet -> holderSet.components.singleOrNull()?.let(::listOf) ?: listOf(holderSet)
                else -> listOf(holderSet)
            }
        }.sortedWith(HOLDER_SET).let(::OrHolderSet)

    /**
     * @since 26.1.7
     */
    @JvmStatic
    fun <T : Any> compressHolderSet(holderSets: Sequence<HolderSet<T>>): OrHolderSet<T> =
        holderSets.flatMap { holderSet: HolderSet<T> ->
            when (holderSet) {
                is CompositeHolderSet -> holderSet.components.singleOrNull()?.let(::listOf) ?: listOf(holderSet)
                else -> listOf(holderSet)
            }
        }.sortedWith(HOLDER_SET).toList().let(::OrHolderSet)
}
