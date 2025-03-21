package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTSimpleFluidRecipe
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTRefiningRecipe(ingredient: SizedFluidIngredient, output: HTFluidOutput) :
    HTSimpleFluidRecipe(RagiumRecipes.REFINING, ingredient, output)
