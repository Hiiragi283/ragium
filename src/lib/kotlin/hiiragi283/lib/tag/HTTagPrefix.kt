package hiiragi283.lib.tag

import hiiragi283.lib.HTConstants
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * タグのプレフィックスを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTTagPrefix(val rawCommonTag: BlockItemTag, private val tagPattern: String) {
    constructor(commonTagId: String, tagPattern: String) : this(BlockItemTag(HTConstants.COMMON, commonTagId), tagPattern)

    /**
     * 素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun materialTag(material: HTTagMaterial): BlockItemTag = BlockItemTag(HTConstants.COMMON, tagPattern.replace("%s", material.materialName))

    /**
     * ブロックの素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun blockTagKey(material: HTTagMaterial): TagKey<Block> = materialTag(material).block

    /**
     * アイテムの素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun itemTagKey(material: HTTagMaterial): TagKey<Item> = materialTag(material).item
}
