package hiiragi283.lib

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
    val KEY: Comparator<ResourceKey<*>> = compareBy(ID, ResourceKey<*>::registry).thenComparing(compareBy(ID, ResourceKey<*>::identifier))

    /**
     * [TagKey]の[Comparator]
     */
    @JvmField
    val TAG_KEY: Comparator<TagKey<*>> = compareBy(KEY, TagKey<*>::registry).thenComparing(compareBy(ID, TagKey<*>::location))
}
