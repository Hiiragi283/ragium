package hiiragi283.lib

import com.google.common.collect.Comparators
import net.minecraft.core.HolderSet
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

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
}
