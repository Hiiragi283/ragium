package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType

class HTBlastFurnaceRecipe(group: String, itemInputs: List<HTItemIngredient>, itemOutput: HTItemOutput) :
    HTMultiItemRecipe(group, itemInputs, itemOutput) {
    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.BLAST_FURNACE
}
