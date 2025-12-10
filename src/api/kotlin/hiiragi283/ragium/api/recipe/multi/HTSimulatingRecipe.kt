package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.crafting.RecipeType

/**
 * 単一のアイテムと触媒から複数の完成品（アイテム，液体）を生産するレシピ
 */
interface HTSimulatingRecipe :
    HTFluidRecipe,
    HTItemIngredient.CountGetter {
    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SIMULATING.get()
}
