package hiiragi283.lib.data.tag

import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.registry.toLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.lib.tag.RawTagKey
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * [Item]向けの[HTTagBuilder]の拡張クラスです。
 *
 * 参照 : [NeoForge - BlockTagCopyingItemTagProvider][net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTItemTagsProvider : HTTagsProvider<Item> {
    private val blockTags: CompletableFuture<TagLookup<Block>>
    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    constructor(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, parentProvider: CompletableFuture<TagLookup<Item>>, contentsGetter: CompletableFuture<TagLookup<Block>>, modId: String) : super(output, Registries.ITEM, lookupProvider, parentProvider, modId) {
        this.blockTags = contentsGetter
    }

    constructor(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>, modId: String) : super(output, Registries.ITEM, lookupProvider, modId) {
        this.blockTags = contentsGetter
    }

    /**
     * コピーするタグを追加します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun copy(prefix: HTTagPrefix, material: HTMaterialKey) {
        this.copy(prefix.rawCommonTag)
        this.copy(prefix.materialTag(material))
    }

    /**
     * コピーするタグを追加します。
     * @param tagKey コピーするタグ
     */
    protected fun copy(tagKey: RawTagKey) {
        this.copy(tagKey.create(Registries.BLOCK), tagKey.create(Registries.ITEM))
    }

    /**
     * コピーするタグを追加します。
     * @param blockTag コピー元となるブロックのタグ
     * @param itemTag コピー先となるアイテムのタグ
     */
    protected fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    /**
     * 指定した要素をタグに追加します。
     * @param item アイテムの値
     */
    @Suppress("DEPRECATION")
    protected fun HTTagBuilder<Item>.addItem(item: ItemLike): HTTagBuilder<Item> = this.add(item.asItem().builtInRegistryHolder().toLike())

    final override fun createContentsProvider(): CompletableFuture<HolderLookup.Provider> = super.createContentsProvider().thenCombine(blockTags) { provider: HolderLookup.Provider, blockTags1: TagLookup<Block> ->
        for ((blockTag: TagKey<Block>, itemTag: TagKey<Item>) in this.tagsToCopy) {
            val builder: TagBuilder = this.getOrCreateRawBuilder(itemTag)
            blockTags1.apply(blockTag)
                .orElseThrow { error("Missing block tag ${itemTag.location()}") }
                .let { blockBuilder ->
                    blockBuilder.build().forEach(builder::add)
                    blockBuilder.removeEntries.forEach(builder::remove)
                }
        }
        provider
    }
}
