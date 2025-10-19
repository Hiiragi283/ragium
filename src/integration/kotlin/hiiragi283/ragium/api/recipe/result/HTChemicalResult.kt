package hiiragi283.ragium.api.recipe.result

import mekanism.api.chemical.ChemicalStack

interface HTChemicalResult : HTRecipeResult<ChemicalStack> {
    fun copyWithAmount(amount: Long): HTChemicalResult
}
