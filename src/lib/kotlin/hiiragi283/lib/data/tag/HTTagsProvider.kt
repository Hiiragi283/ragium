package hiiragi283.lib.data.tag

import hiiragi283.lib.collection.SetMultiMap
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

/**
 * Hiiragi Seriesで使用される[TagsProvider]の拡張クラスです。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTTagsProvider<T : Any> : TagsProvider<T> {
    companion object {
        /**
         * タグの生成時に使用されるソーター
         */
        @JvmField
        val COMPARATOR: Comparator<TagEntry> = Comparator
            .comparing(TagEntry::isRequired)
            .thenComparing(TagEntry::isTag, Comparator.reverseOrder())
            .thenComparing(TagEntry::getId)
    }

    constructor(output: PackOutput, registryKey: RegistryKey<T>, lookupProvider: CompletableFuture<HolderLookup.Provider>, parentProvider: CompletableFuture<TagLookup<T>>, modId: String) : super(output, registryKey, lookupProvider, parentProvider, modId)

    constructor(output: PackOutput, registryKey: RegistryKey<T>, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : super(output, registryKey, lookupProvider, modId)

    private val entryCache = SetMultiMap.Builder<TagKey<T>, TagEntry>()

    final override fun addTags(registries: HolderLookup.Provider) {
        appendTags(registries)

        entryCache.build().asMap().forEach { (tagKey: TagKey<T>, entries: Collection<TagEntry>) ->
            entries
                .sortedWith(COMPARATOR)
                .distinctBy(TagEntry::toString)
                .forEach { entry: TagEntry -> getOrCreateRawBuilder(tagKey).add(entry) }
        }
    }

    /**
     * 生成するタグを登録します。
     */
    protected abstract fun appendTags(registries: HolderLookup.Provider)

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param tagKey 生成対象のタグ
     */
    protected fun builder(tagKey: TagKey<T>): HTTagBuilder<T> = HTTagBuilder { entry: TagEntry -> entryCache.put(tagKey, entry) }

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun tags(prefix: HTTagPrefix, material: HTMaterialKey): HTTagBuilder<T> = tags(prefix.rawCommonTag.create(registryKey), prefix.materialTag(material).create(registryKey))

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param tagKey 起点となるタグ
     * @param children [tagKey]からチェインして生成するタグ
     * @return [children]の最後の値に対する[HTTagBuilder]
     */
    protected fun tags(tagKey: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> = children.fold(builder(tagKey)) { builder: HTTagBuilder<T>, tagKeyIn: TagKey<T> ->
        builder.addTag(tagKeyIn)
        builder(tagKeyIn)
    }

    /**
     * @since 26.1.3
     */
    protected fun createKey(id: Identifier): ResourceKey<T> = registryKey.createKey(id)

    /**
     * @since 26.1.3
     */
    protected fun createKey(namespace: String, path: String): ResourceKey<T> = registryKey.createKey(namespace, path)
}
