package hiiragi283.ragium.api.data.recipe.ingredient

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * [HTItemIngredient]を返す[HTIngredientCreator]の拡張インターフェース
 * @see mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
 */
interface HTItemIngredientCreator : HTIngredientCreator<Item, HTItemIngredient> {
    // Ingredient
    fun from(ingredient: Ingredient, count: Int = 1): HTItemIngredient {
        check(!ingredient.isEmpty) { "Empty ingredient is not allowed" }
        return HTItemIngredient(ingredient, count)
    }

    // Default Count
    fun fromItem(item: ItemLike, count: Int = 1): HTItemIngredient = from(item.asItem(), count)

    fun fromItems(items: Iterable<ItemLike>, count: Int = 1): HTItemIngredient = from(items.map(ItemLike::asItem), count)

    fun fromTagKey(tagKey: TagKey<Item>): HTItemIngredient = fromTagKey(tagKey, 1)

    fun fromTagKeys(tagKeys: List<TagKey<Item>>): HTItemIngredient = fromTagKeys(tagKeys, 1)

    // Material
    fun fromTagKey(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTItemIngredient =
        fromTagKey(prefix.itemTagKey(material), count)

    fun multiPrefixes(material: HTMaterialLike, vararg prefix: HTPrefixLike, count: Int = 1): HTItemIngredient =
        fromTagKeys(prefix.map { it.itemTagKey(material) }, count)
}
