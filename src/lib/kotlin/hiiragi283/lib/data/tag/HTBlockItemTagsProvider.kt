package hiiragi283.lib.data.tag

import hiiragi283.lib.resource.BlockItemKey
import hiiragi283.lib.resource.HTBlockItemWithKey
import hiiragi283.lib.tag.BlockItemTag
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * [BlockItemKey]と[BlockItemTag]向けにタグを生成するクラスです。
 * @param factory [BlockItemTag]から[ビルダー][HTBlockItemTagBuilder]を作成するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
abstract class HTBlockItemTagsProvider(private val factory: (BlockItemTag) -> HTBlockItemTagBuilder) {
    companion object {
        /**
         * [HTTagBuilder]を[HTBlockItemTagBuilder]に変換します。
         */
        @JvmStatic
        fun forBlock(builder: HTTagBuilder<Block>): HTBlockItemTagBuilder = object : HTBlockItemTagBuilder {
            override fun add(key: BlockItemKey, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.add(key.block, type) }

            override fun add(value: HTBlockItemWithKey<*, *>, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.add(value.block, type) }

            override fun addTag(child: BlockItemTag, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.addTag(child.block, type) }
        }

        /**
         * [HTTagBuilder]を[HTBlockItemTagBuilder]に変換します。
         */
        @JvmStatic
        fun forItem(builder: HTTagBuilder<Item>): HTBlockItemTagBuilder = object : HTBlockItemTagBuilder {
            override fun add(key: BlockItemKey, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.add(key.item, type) }

            override fun add(value: HTBlockItemWithKey<*, *>, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.add(value.item, type) }

            override fun addTag(child: BlockItemTag, type: HTTagDependType): HTBlockItemTagBuilder =
                apply { builder.addTag(child.item, type) }
        }
    }

    /**
     * タグを登録します。
     */
    abstract fun run()

    protected fun builder(tagKey: BlockItemTag): HTBlockItemTagBuilder = factory(tagKey)

    protected fun builders(tagKey: BlockItemTag, vararg children: BlockItemTag): HTBlockItemTagBuilder =
        children.fold(builder(tagKey)) { builder: HTBlockItemTagBuilder, tagKeyIn: BlockItemTag ->
            builder.addTag(tagKeyIn)
            builder(tagKeyIn)
        }

    protected fun builder(prefix: HTTagPrefix, material: HTMaterialLike): HTBlockItemTagBuilder =
        builders(prefix.rawCommonTag, prefix.materialTag(material))
}
