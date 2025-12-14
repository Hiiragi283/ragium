package hiiragi283.ragium.api.recipe.fluid

import hiiragi283.ragium.api.recipe.HTSingleItemInputRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeType

interface HTExtractingRecipe :
    HTFluidRecipe,
    HTSingleItemInputRecipe {
    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
