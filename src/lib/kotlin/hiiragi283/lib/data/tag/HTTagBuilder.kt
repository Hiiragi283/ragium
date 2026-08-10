package hiiragi283.lib.data.tag

import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.lib.tag.RawTagKey
import hiiragi283.lib.util.HTBuilderMarker
import java.util.function.Consumer
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

/**
 * [HTTagsProvider]で使用されるタグのビルダークラスです。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
fun interface HTTagBuilder<R : Any> : Consumer<TagEntry> {
    /**
     * 指定した要素をタグに追加します。
     * @param key 要素のキー
     * @param type このエントリの依存関係
     */
    fun add(key: ResourceKey<R>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<R> = add(key.identifier(), type)

    /**
     * 指定した要素をタグに追加します。
     * @param like 要素のキーを提供する[HTKeyLike]
     * @param type このエントリの依存関係
     * @since 26.1.3
     */
    fun add(like: HTKeyLike<R>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<R> = add(like.getKey(), type)

    /**
     * 指定した要素をタグに追加します。
     * @param id 要素のID
     * @param type このエントリの依存関係
     */
    private fun add(id: Identifier, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<R> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.element(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalElement(id)
        }.let(this::accept)
    }

    /**
     * 指定した子タグをタグに追加します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     * @param type このエントリの依存関係
     */
    fun addTag(prefix: HTTagPrefix, material: HTMaterialKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<R> = addTag(prefix.materialTag(material), type)

    /**
     * 指定した子タグをタグに追加します。
     * @param child 子タグ
     * @param type このエントリの依存関係
     */
    fun addTag(child: TagKey<R>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<R> = addTag(child.location, type)

    /**
     * 指定した子タグをタグに追加します。
     * @param child 子タグとなる[RawTagKey]
     * @param type このエントリの依存関係
     */
    fun addTag(child: RawTagKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<R> = addTag(child.location, type)

    /**
     * 指定した子タグをタグに追加します。
     * @param id 子タグのID
     * @param type このエントリの依存関係
     */
    private fun addTag(id: Identifier, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<R> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.tag(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalTag(id)
        }.let(this::accept)
    }
}
