package hiiragi283.lib.material.property

import hiiragi283.lib.material.HTMaterialAccess
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.registry.getOrNull
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.util.Ior
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * 素材のデフォルトのアイテムを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
sealed interface HTDefaultPart {
    /**
     * 指定した[素材][key]から素材アイテムのタグを取得します。
     */
    fun getTag(key: HTMaterialKey): TagKey<Item>?

    /**
     * 指定した[素材][key]から素材アイテムのタグを取得します。
     */
    fun getHolderSet(key: HTMaterialKey, getter: HolderGetter<Item>): HolderSet.Named<Item>? = getTag(key)?.let(getter::getOrNull)

    /**
     * 指定した[素材][key]から素材アイテムを取得します。
     * @return 対応するアイテムがない場合は`null`
     */
    fun getItem(key: HTMaterialKey): HTMaterialContents.ItemEntry?

    /**
     * レシピの生成時に使用されるサフィックスを取得します。
     */
    fun getSuffix(): String

    /**
     * 既存の[TagKey]と[Item]に基づいた[HTDefaultPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    @JvmRecord
    data class BuiltIn(val content: Ior<TagKey<Item>, HTSimpleDeferredItem>) : HTDefaultPart {
        constructor(tagKey: TagKey<Item>) : this(Ior.Left(tagKey))

        constructor(id: Identifier) : this(HTSimpleDeferredItem(id))

        constructor(key: ResourceKey<Item>) : this(HTSimpleDeferredItem(key))

        constructor(holder: HTSimpleDeferredItem) : this(Ior.Right(holder))

        constructor(tagKey: TagKey<Item>, id: Identifier) : this(Ior.Both(tagKey, HTSimpleDeferredItem(id)))

        constructor(tagKey: TagKey<Item>, key: ResourceKey<Item>) : this(Ior.Both(tagKey, HTSimpleDeferredItem(key)))

        constructor(tagKey: TagKey<Item>, holder: HTSimpleDeferredItem) : this(Ior.Both(tagKey, holder))

        val tagKey: TagKey<Item>? get() = content.getLeft()
        val holder: HTSimpleDeferredItem? get() = content.getRight()

        override fun getTag(key: HTMaterialKey): TagKey<Item>? = tagKey

        override fun getItem(key: HTMaterialKey): HTMaterialContents.ItemEntry? = holder?.let { HTMaterialContents.ItemEntry(it, true) }

        override fun getSuffix(): String = content.map(TagKey<Item>::location, HTSimpleDeferredItem::getId).path
    }

    data object Gem : HTDefaultPart {
        override fun getTag(key: HTMaterialKey): TagKey<Item> = CommonTagPrefixes.GEM.itemTagKey(key)

        override fun getItem(key: HTMaterialKey): HTMaterialContents.ItemEntry? = HTMaterialAccess.INSTANCE.getMaterialBlockOrItem(CommonParts.GEM, key)

        override fun getSuffix(): String = "gem"
    }

    data object Ingot : HTDefaultPart {
        override fun getTag(key: HTMaterialKey): TagKey<Item> = CommonTagPrefixes.INGOT.itemTagKey(key)

        override fun getItem(key: HTMaterialKey): HTMaterialContents.ItemEntry? = HTMaterialAccess.INSTANCE.getMaterialBlockOrItem(CommonParts.INGOT, key)

        override fun getSuffix(): String = "ingot"
    }
}
