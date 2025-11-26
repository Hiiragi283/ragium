package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * バニラの[Ingredient]を使用するレシピ向けのビルダー
 * @param BUILDER [HTIngredientRecipeBuilder]を継承したクラス
 */
interface HTIngredientRecipeBuilder<BUILDER : HTIngredientRecipeBuilder<BUILDER>> {
    fun addIngredient(prefix: HTPrefixLike, material: HTMaterialLike): BUILDER = addIngredient(prefix.itemTagKey(material))

    fun addIngredient(tagKey: TagKey<Item>): BUILDER = addIngredient(Ingredient.of(tagKey))

    fun addIngredient(vararg items: ItemLike): BUILDER = addIngredient(Ingredient.of(*items))

    fun addIngredient(ingredient: Ingredient): BUILDER
}
