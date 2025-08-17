package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * バニラの[Ingredient]を使用するレシピ向けの[HTRecipeBuilder]
 * @param B [HTRecipeBuilder]を継承したクラス
 */
interface HTIngredientRecipeBuilder<B : HTIngredientRecipeBuilder<B>> : HTRecipeBuilder {
    fun addIngredient(variant: HTMaterialVariant, material: HTMaterialType): B = addIngredient(variant.itemTagKey(material))

    fun addIngredient(tagKey: TagKey<Item>): B = addIngredient(Ingredient.of(tagKey))

    fun addIngredient(item: ItemLike): B = addIngredient(Ingredient.of(item))

    fun addIngredient(ingredient: Ingredient): B
}
