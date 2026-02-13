package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTSqueezingRecipe(ingredient: HTItemIngredient, result: HTFluidResult, parameters: SubParameters) :
    HTSingleProcessingRecipe.ItemToFluid(ingredient, result, parameters) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SQUEEZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SQUEEZING.get()
}
