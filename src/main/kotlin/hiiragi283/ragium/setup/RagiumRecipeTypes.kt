package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe

typealias HTDeferredViewRecipeType<RECIPE> = HTDeferredRecipeType<HTViewRecipeInput, RECIPE>

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTDeferredViewRecipeType<HTAlloyingRecipe> = REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val CRUSHING: HTDeferredViewRecipeType<HTCrushingRecipe> = REGISTER.registerType(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredViewRecipeType<HTCuttingRecipe> = REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val PRESSING: HTDeferredViewRecipeType<HTPressingRecipe> = REGISTER.registerType(RagiumConst.PRESSING)

    // Machine - Heat
    @JvmField
    val MELTING: HTDeferredViewRecipeType<HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTDeferredViewRecipeType<HTPyrolyzingRecipe> = REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTDeferredViewRecipeType<HTRefiningRecipe> = REGISTER.registerType(RagiumConst.REFINING)

    @JvmField
    val SOLIDIFYING: HTDeferredViewRecipeType<HTSolidifyingRecipe> = REGISTER.registerType(RagiumConst.SOLIDIFYING)

    // Machine - Chemical
    @JvmField
    val MIXING: HTDeferredViewRecipeType<HTMixingRecipe> = REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val WASHING: HTDeferredViewRecipeType<HTWashingRecipe> = REGISTER.registerType(RagiumConst.WASHING)

    // Machine - Matter

    // Device - Basic
    @JvmField
    val PLANTING: HTDeferredViewRecipeType<HTPlantingRecipe> = REGISTER.registerType(RagiumConst.PLANTING)

    // Device - Advanced

    // Device - Enchanting
    @JvmField
    val ENCHANTING: HTDeferredViewRecipeType<HTEnchantingRecipe> = REGISTER.registerType(RagiumConst.ENCHANTING)
}
