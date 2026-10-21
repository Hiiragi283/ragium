package hiiragi283.lib.data.tag

import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.references.BlockItemId
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * [BlockItemId]と[BlockItemTagId]向けにタグを生成するクラスです。
 * @param factory [BlockItemTagId]から[ビルダー][HTBlockItemTagBuilder]を作成するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
abstract class HTBlockItemTagsProvider(private val factory: (BlockItemTagId) -> HTBlockItemTagBuilder) {
    companion object {
        /**
         * [HTTagBuilder]を[HTBlockItemTagBuilder]に変換します。
         */
        @JvmStatic
        fun forBlock(builder: HTTagBuilder<Block>): HTBlockItemTagBuilder = object : HTBlockItemTagBuilder {
            override fun add(key: BlockItemId, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.add(key.block(), type) }

            override fun addTag(child: BlockItemTagId, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.addTag(child.block(), type) }
        }

        /**
         * [HTTagBuilder]を[HTBlockItemTagBuilder]に変換します。
         */
        @JvmStatic
        fun forItem(builder: HTTagBuilder<Item>): HTBlockItemTagBuilder = object : HTBlockItemTagBuilder {
            override fun add(key: BlockItemId, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.add(key.item(), type) }

            override fun addTag(child: BlockItemTagId, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.addTag(child.item(), type) }
        }
    }

    /**
     * タグを登録します。
     */
    abstract fun run()

    protected fun builder(tagKey: BlockItemTagId): HTBlockItemTagBuilder = factory(tagKey)

    /**
     * @since 26.1.8
     */
    protected fun builder(block: TagKey<Block>, item: TagKey<Item>): HTBlockItemTagBuilder =
        builder(BlockItemTagId(block, item))

    protected fun builders(tagKey: BlockItemTagId, vararg children: BlockItemTagId): HTBlockItemTagBuilder =
        children.fold(builder(tagKey)) { builder: HTBlockItemTagBuilder, tagKeyIn: BlockItemTagId ->
            builder.addTag(tagKeyIn)
            builder(tagKeyIn)
        }

    protected fun builder(prefix: HTTagPrefix, material: HTMaterialLike): HTBlockItemTagBuilder =
        builders(prefix.rawCommonTag, prefix.materialTag(material))
}
