@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.tag

import hiiragi283.lib.collection.SetMultiMap
import hiiragi283.lib.data.pack.HTDynamicDataRegister
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.tag.HTTagMaterial
import hiiragi283.lib.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagFile
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * [HTTagBuilder]に基づいてタグを生成するインターフェースです。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
interface HTTagsProvider<T : Any> {
    companion object {
        /**
         * タグの生成時に使用されるソーター
         */
        @JvmField
        val TAG_ENTRY_COMPARATOR: Comparator<TagEntry> = Comparator
            .comparing(TagEntry::isRequired)
            .thenComparing(TagEntry::isTag, Comparator.reverseOrder())
            .thenComparing(TagEntry::getId)
    }

    /**
     * レジストリのキー
     */
    val registryKey: RegistryKey<T>

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param tagKey 生成対象のタグ
     */
    fun builder(tagKey: TagKey<T>): HTTagBuilder<T>

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param tagKey 起点となるタグ
     * @param children [tagKey]からチェインして生成するタグ
     * @return [children]の最後の値に対する[HTTagBuilder]
     */
    fun builders(tagKey: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> = children.fold(builder(tagKey)) { builder: HTTagBuilder<T>, tagKeyIn: TagKey<T> ->
        builder.addTag(tagKeyIn)
        builder(tagKeyIn)
    }

    /**
     * 新しい[ResourceKey]のインスタンスを作成します。
     */
    fun createKey(id: Identifier): ResourceKey<T> = registryKey.createKey(id)

    /**
     * 新しい[ResourceKey]のインスタンスを作成します。
     */
    fun createKey(namespace: String, path: String): ResourceKey<T> = registryKey.createKey(namespace, path)

    //    Dynamic    //

    /**
     * 動的データパック向けの[TagsProvider]の代替クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    class Dynamic<T : Any>(override val registryKey: RegistryKey<T>) : HTTagsProvider<T> {
        companion object {
            @JvmStatic
            inline operator fun <T : Any> invoke(registryKey: RegistryKey<T>, action: Dynamic<T>.() -> Unit) {
                contract {
                    callsInPlace(action, InvocationKind.EXACTLY_ONCE)
                }
                Dynamic(registryKey).apply(action).addTags()
            }
        }

        private val entryCache = SetMultiMap.SortedBuilder<TagKey<T>, TagEntry>(TAG_ENTRY_COMPARATOR)

        override fun builder(tagKey: TagKey<T>): HTTagBuilder<T> = HTTagBuilder { entry: TagEntry -> entryCache.put(tagKey, entry) }

        fun addTags() {
            entryCache.build().entries.forEach { (tagKey: TagKey<T>, entries: Collection<TagEntry>) ->
                HTDynamicDataRegister.addToData(
                    getTagPath(tagKey),
                    TagFile.CODEC,
                    TagFile(entries.toList(), false, listOf()),
                )
            }
        }

        private fun getTagPath(tagKey: TagKey<T>): Identifier = tagKey.location().withPrefix("${Registries.tagsDirPath(tagKey.registry())}/")
    }

    //    DataGen    //

    /**
     * [HTTagsProvider]に基づいて[TagsProvider]を実装した抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    abstract class DataGen<T : Any>(
        output: PackOutput,
        override val registryKey: RegistryKey<T>,
        lookupProvider: CompletableFuture<HolderLookup.Provider>,
        modId: String,
    ) : TagsProvider<T>(output, registryKey, lookupProvider, modId),
        HTTagsProvider<T> {
        private val entryCache = SetMultiMap.SortedBuilder<TagKey<T>, TagEntry>(TAG_ENTRY_COMPARATOR)

        final override fun builder(tagKey: TagKey<T>): HTTagBuilder<T> = HTTagBuilder { entry: TagEntry -> entryCache.put(tagKey, entry) }

        final override fun addTags(provider: HolderLookup.Provider) {
            createEmptyTags(provider, ::getOrCreateRawBuilder)

            appendTags(provider)

            entryCache.build().asMap().forEach { (tagKey: TagKey<T>, entries: Collection<TagEntry>) ->
                entries
                    .distinctBy(TagEntry::toString)
                    .forEach { entry: TagEntry -> getOrCreateRawBuilder(tagKey).add(entry) }
            }
        }

        protected open fun createEmptyTags(registries: HolderLookup.Provider, consumer: (TagKey<T>) -> Unit) {}

        /**
         * 生成するタグを登録します。
         */
        protected abstract fun appendTags(registries: HolderLookup.Provider)

        protected fun createEmptyTag(tagKey: TagKey<T>) {
            getOrCreateRawBuilder(tagKey)
        }
    }
}

//    Extensions    //

/**
 * 新しい[HTTagBuilder]のインスタンスを作成します。
 * @param prefix タグのプレフィックス
 * @param material タグの種類を表す素材
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
@JvmName("blockBuilders")
fun HTTagsProvider<Block>.builders(prefix: HTTagPrefix, material: HTTagMaterial): HTTagBuilder<Block> = builders(prefix.rawCommonTag.block, prefix.blockTagKey(material))

/**
 * 新しい[HTTagBuilder]のインスタンスを作成します。
 * @param prefix タグのプレフィックス
 * @param material タグの種類を表す素材
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
@JvmName("itemBuilders")
fun HTTagsProvider<Item>.builders(prefix: HTTagPrefix, material: HTTagMaterial): HTTagBuilder<Item> = builders(prefix.rawCommonTag.item, prefix.itemTagKey(material))
