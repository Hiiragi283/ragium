package hiiragi283.lib.tag

import hiiragi283.lib.material.HTMaterialKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * タグのプレフィックスを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTTagPrefix(val rawCommonTag: RawTagKey, private val tagPattern: String) {
    constructor(commonTagId: String, tagPattern: String) : this(RawTagKey.common(commonTagId), tagPattern)

    /**
     * 素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun materialTag(material: HTMaterialKey): RawTagKey = RawTagKey.common(tagPattern.replace("%s", material.path))

    /**
     * アイテムの素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun itemTagKey(material: HTMaterialKey): TagKey<Item> = materialTag(material).create(Registries.ITEM)
}
