package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.material.HTMaterialType
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

/**
 * 素材の形態を表現する[HTVariantKey]の拡張インターフェース
 */
interface HTMaterialVariant : HTVariantKey {
    /**
     * [Item]の[TagKey]を生成可能な[HTMaterialVariant]の拡張インターフェース
     */
    interface ItemTag : HTMaterialVariant {
        /**
         * 素材の形態を表す[TagKey]
         */
        val itemCommonTag: TagKey<Item>?

        fun canGenerateTag(): Boolean

        /**
         * 指定した[HTMaterialType]を[TagKey]に変換します。
         */
        fun itemTagKey(material: HTMaterialType): TagKey<Item> = itemTagKey(material.materialName())

        /**
         * 指定した[String]を[TagKey]に変換します。
         */
        fun itemTagKey(path: String): TagKey<Item>

        fun toIngredient(material: HTMaterialType): Ingredient = Ingredient.of(itemTagKey(material))

        fun toIngredient(path: String): Ingredient = Ingredient.of(itemTagKey(path))
    }

    /**
     * [Block]の[TagKey]を生成可能な[ItemTag]の拡張インターフェース
     */
    interface BlockTag : ItemTag {
        /**
         * 素材の形態を表す[TagKey]
         */
        val blockCommonTag: TagKey<Block>?

        /**
         * 指定した[HTMaterialType]を[TagKey]に変換します。
         */
        fun blockTagKey(material: HTMaterialType): TagKey<Block> = blockTagKey(material.materialName())

        /**
         * 指定した[String]を[TagKey]に変換します。
         */
        fun blockTagKey(path: String): TagKey<Block>
    }
}
