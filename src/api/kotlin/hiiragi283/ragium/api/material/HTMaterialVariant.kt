package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.variant.HTVariantKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

interface HTMaterialVariant : HTVariantKey {
    interface ItemTag : HTMaterialVariant {
        val itemCommonTag: TagKey<Item>?

        fun canGenerateTag(): Boolean

        fun itemTagKey(material: HTMaterialType): TagKey<Item> = itemTagKey(material.serializedName)

        fun itemTagKey(path: String): TagKey<Item>

        fun toIngredient(material: HTMaterialType): Ingredient = Ingredient.of(itemTagKey(material))

        fun toIngredient(path: String): Ingredient = Ingredient.of(itemTagKey(path))
    }

    interface BlockTag : ItemTag {
        val blockCommonTag: TagKey<Block>?

        fun blockTagKey(material: HTMaterialType): TagKey<Block> = blockTagKey(material.serializedName)

        fun blockTagKey(path: String): TagKey<Block>
    }
}
