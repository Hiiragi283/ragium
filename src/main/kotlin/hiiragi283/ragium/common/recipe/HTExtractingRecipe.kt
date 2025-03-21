package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTExtractingRecipe(ingredient: SizedIngredient, itemOutput: HTItemOutput) :
    HTSimpleItemRecipe(
        RagiumRecipes.EXTRACTING,
        ingredient,
        itemOutput,
    )
