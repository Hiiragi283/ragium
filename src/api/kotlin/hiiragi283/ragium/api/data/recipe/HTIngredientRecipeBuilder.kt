package hiiragi283.ragium.api.data.recipe

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * バニラの[Ingredient]を使用するレシピ向けの[HTRecipeBuilder]
 * @param B [HTRecipeBuilder]を継承したクラス
 */
interface HTIngredientRecipeBuilder<B : HTIngredientRecipeBuilder<B>> : HTRecipeBuilder {
    fun addIngredient(tagKey: TagKey<Item>): B = addIngredient(Ingredient.of(tagKey))

    fun addIngredient(item: ItemLike): B = addIngredient(Ingredient.of(item))

    fun addIngredient(ingredient: Ingredient): B
}
