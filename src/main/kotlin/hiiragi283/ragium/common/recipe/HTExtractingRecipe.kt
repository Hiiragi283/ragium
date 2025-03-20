package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTExtractingRecipe(ingredient: SizedIngredient, itemOutput: HTItemOutput, fluidOutput: HTFluidOutput?) :
    HTItemProcessRecipe(
        RagiumRecipes.EXTRACTING,
        ingredient,
        itemOutput,
        fluidOutput,
    )
