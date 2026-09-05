package hiiragi283.lib.tag

import hiiragi283.lib.HTConstants
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * タグのプレフィックスを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTTagPrefix(
    val rawCommonTag: BlockItemTag,
    private val tagPattern: String,
    private val childNamespace: String = HTConstants.COMMON
) {
    constructor(
        commonTagId: Identifier,
        tagPattern: String,
        childNamespace: String = HTConstants.COMMON
    ) : this(BlockItemTag(commonTagId), tagPattern, childNamespace)

    constructor(
        commonTagId: String,
        tagPattern: String,
        childNamespace: String = HTConstants.COMMON
    ) : this(BlockItemTag(HTConstants.COMMON, commonTagId), tagPattern, childNamespace)

    /**
     * 素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun materialTag(material: HTMaterialLike): BlockItemTag =
        BlockItemTag(childNamespace, tagPattern.replace("%s", material.materialName))

    /**
     * ブロックの素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun blockTagKey(material: HTMaterialLike): TagKey<Block> = materialTag(material).block

    /**
     * アイテムの素材の共通タグを生成します。
     * @param material 素材の種類
     */
    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = materialTag(material).item
}
