package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe

object RagiumRecipeTypes {
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTRecipeInput, HTAlloyingRecipe> = create(RagiumConst.ALLOYING)

    @JvmStatic
    private fun <RECIPE : HTRecipe> create(path: String): HTDeferredRecipeType<HTRecipeInput, RECIPE> =
        HTDeferredRecipeType(RagiumAPI.id(path))
}
