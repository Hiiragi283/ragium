package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTMixingRecipe(
    itemIngredients: List<HTItemIngredient>,
    fluidIngredients: List<HTFluidIngredient>,
    results: Ior<HTItemResult, HTFluidResult>,
) : HTBasicComplexRecipe(itemIngredients, fluidIngredients, results) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
