package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.references.BlockItemId
import net.minecraft.tags.BlockItemTagId

/**
 * [HTBlockItemTagsProvider]で使用されるタグのビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
interface HTBlockItemTagBuilder {
    /**
     * 指定した要素をタグに追加します。
     * @param key 要素のキー
     * @param type このエントリの依存関係
     */
    fun add(key: BlockItemId, type: HTTagDependType = HTTagDependType.REQUIRED): HTBlockItemTagBuilder

    /**
     * 指定した要素をタグに追加します。
     * @param value 要素のキーを提供する[HTSimpleDeferredBlockAndItem]
     * @param type このエントリの依存関係
     */
    fun add(
        value: HTSimpleDeferredBlockAndItem,
        type: HTTagDependType = HTTagDependType.REQUIRED
    ): HTBlockItemTagBuilder = this.add(value.blockItemId, type)

    /**
     * 指定した子タグをタグに追加します。
     * @param child 子タグ
     * @param type このエントリの依存関係
     */
    fun addTag(child: BlockItemTagId, type: HTTagDependType = HTTagDependType.REQUIRED): HTBlockItemTagBuilder

    /**
     * 指定した子タグをタグに追加します。
     * @param prefix タグのプレフィックス
     * @param material タグの素材
     * @param type このエントリの依存関係
     */
    fun addTag(
        prefix: HTTagPrefix,
        material: HTMaterialLike,
        type: HTTagDependType = HTTagDependType.REQUIRED
    ): HTBlockItemTagBuilder = this.addTag(prefix.materialTag(material), type)
}
