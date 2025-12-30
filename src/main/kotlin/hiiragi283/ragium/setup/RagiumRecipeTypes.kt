package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTRecipeInput, HTAlloyingRecipe> = REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<HTRecipeInput, HTCrushingRecipe> = REGISTER.registerType(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<HTRecipeInput, HTCuttingRecipe> = REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val DRYING: HTDeferredRecipeType<HTRecipeInput, HTDryingRecipe> = REGISTER.registerType(RagiumConst.DRYING)

    @JvmField
    val MELTING: HTDeferredRecipeType<HTRecipeInput, HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTDeferredRecipeType<HTRecipeInput, HTPyrolyzingRecipe> = REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTRecipeInput, HTRefiningRecipe> = REGISTER.registerType(RagiumConst.REFINING)
}
