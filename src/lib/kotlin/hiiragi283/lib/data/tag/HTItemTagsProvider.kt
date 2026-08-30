package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.asSupplier
import hiiragi283.lib.tag.BlockItemTag
import hiiragi283.lib.tag.HTTagMaterial
import hiiragi283.lib.tag.HTTagPrefix
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
abstract class HTItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, private val blockTags: CompletableFuture<TagLookup<Block>>, modId: String) : HTTagsProvider.DataGen<Item>(output, Registries.ITEM, lookupProvider, modId) {
    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    /**
     * コピーするタグを追加します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun copy(prefix: HTTagPrefix, material: HTTagMaterial) {
        this.copy(prefix.rawCommonTag)
        this.copy(prefix.materialTag(material))
    }

    /**
     * コピーするタグを追加します。
     * @param tagKey コピーするタグ
     */
    protected fun copy(tagKey: BlockItemTag) {
        this.copy(tagKey.block, tagKey.item)
    }

    /**
     * コピーするタグを追加します。
     * @param blockTag コピー元となるブロックのタグ
     * @param itemTag コピー先となるアイテムのタグ
     */
    protected fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    //    Extensions    //

    /**
     * 指定した要素をタグに追加します。
     * @param item アイテムの値
     */
    protected fun HTTagBuilder<Item>.addItem(item: ItemLike): HTTagBuilder<Item> = this.add(item.asItem().asSupplier())

    //    TagsProvider    //

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
