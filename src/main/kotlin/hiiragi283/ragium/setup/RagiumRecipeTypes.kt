package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTRecipeInput, HTAlloyingRecipe> = REGISTER.registerType(RagiumConst.ALLOYING)
}
