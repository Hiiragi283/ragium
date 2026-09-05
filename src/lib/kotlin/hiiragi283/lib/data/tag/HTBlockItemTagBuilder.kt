package hiiragi283.lib.data.tag

import hiiragi283.lib.resource.BlockItemKey
import hiiragi283.lib.resource.HTSimpleBlockItemWithKey
import hiiragi283.lib.tag.BlockItemTag

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
    fun add(key: BlockItemKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTBlockItemTagBuilder

    /**
     * 指定した要素をタグに追加します。
     * @param value 要素のキーを提供する[HTSimpleBlockItemWithKey]
     * @param type このエントリの依存関係
     */
    fun add(value: HTSimpleBlockItemWithKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTBlockItemTagBuilder =
        this.add(value.keyOrThrow, type)

    /**
     * 指定した子タグをタグに追加します。
     * @param child 子タグ
     * @param type このエントリの依存関係
     */
    fun addTag(child: BlockItemTag, type: HTTagDependType = HTTagDependType.REQUIRED): HTBlockItemTagBuilder
}
