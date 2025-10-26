package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.variant.HTMaterialVariant
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * バニラの[Ingredient]を使用するレシピ向けの[HTRecipeBuilder]
 * @param BUILDER [HTRecipeBuilder]を継承したクラス
 */
interface HTIngredientRecipeBuilder<BUILDER : HTIngredientRecipeBuilder<BUILDER>> : HTRecipeBuilder {
    fun addIngredient(variant: HTMaterialVariant.ItemTag, material: HTMaterialType): BUILDER = addIngredient(variant.itemTagKey(material))

    fun addIngredient(tagKey: TagKey<Item>): BUILDER = addIngredient(Ingredient.of(tagKey))

    fun addIngredient(vararg items: ItemLike): BUILDER = addIngredient(Ingredient.of(*items))

    fun addIngredient(ingredient: Ingredient): BUILDER
}
