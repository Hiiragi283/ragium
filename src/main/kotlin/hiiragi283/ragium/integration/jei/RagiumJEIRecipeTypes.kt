package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.recipe.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipes
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

object RagiumJEIRecipeTypes {
    @JvmField
    val CRUSHING: RecipeType<RecipeHolder<HTCrushingRecipe>> = RecipeType.createFromVanilla(RagiumRecipes.CRUSHING)
}
