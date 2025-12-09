package hiiragi283.ragium.api.data.recipe.ingredient

import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import net.minecraft.tags.TagKey

/**
 * [HTIngredient]を作成するヘルパーのインターフェース
 * @param TYPE タイプのクラス
 * @param INGREDIENT [HTIngredient]の実装
 * @see mekanism.api.recipes.ingredients.creator.IIngredientCreator
 */
interface HTIngredientCreator<TYPE : Any, INGREDIENT : HTIngredient<TYPE, *>> {
    // Type
    fun from(type: TYPE, amount: Int): INGREDIENT

    fun from(types: Collection<TYPE>, amount: Int): INGREDIENT

    // TagKey
    fun fromTagKey(tagKey: TagKey<TYPE>, amount: Int): INGREDIENT

    fun fromTagKeys(tagKeys: Collection<TagKey<TYPE>>, amount: Int): INGREDIENT
}
