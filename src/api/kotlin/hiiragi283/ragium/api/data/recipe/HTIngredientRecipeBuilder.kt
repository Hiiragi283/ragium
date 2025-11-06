package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * バニラの[Ingredient]を使用するレシピ向けの[HTRecipeBuilder]
 * @param BUILDER [HTRecipeBuilder]を継承したクラス
 */
interface HTIngredientRecipeBuilder<BUILDER : HTIngredientRecipeBuilder<BUILDER>> : HTRecipeBuilder {
    fun addIngredient(prefix: HTPrefixLike, key: HTMaterialLike): BUILDER = addIngredient(prefix.itemTagKey(key))

    fun addIngredient(tagKey: TagKey<Item>): BUILDER = addIngredient(Ingredient.of(tagKey))

    fun addIngredient(vararg items: ItemLike): BUILDER = addIngredient(Ingredient.of(*items))

    fun addIngredient(ingredient: Ingredient): BUILDER
}
