package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTAlloyingRecipe> = REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val ASSEMBLING: HTDeferredRecipeType<HTAssemblingRecipe> = REGISTER.registerType(RagiumConst.ASSEMBLING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<HTSingleMultiOutputRecipe.Serializable> = REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTDoubleMultiOutputRecipe.Serializable> = REGISTER.registerType(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTDeferredRecipeType<HTFreezingRecipe> = REGISTER.registerType(RagiumConst.FREEZING)

    @JvmField
    val IMPLODING: HTDeferredRecipeType<HTDoubleMultiOutputRecipe.Serializable> = REGISTER.registerType(RagiumConst.IMPLODING)

    @JvmField
    val MELTING: HTDeferredRecipeType<HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTDeferredRecipeType<HTItemOrFluidRecipe.Serializable> = REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTItemOrFluidRecipe.Serializable> = REGISTER.registerType(RagiumConst.REFINING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTWashingRecipe> = REGISTER.registerType(RagiumConst.WASHING)

    // Machine - Elite
    @JvmField
    val CHEMICAL_REACTING: HTDeferredRecipeType<HTChemicalReactingRecipe> = REGISTER.registerType(RagiumConst.CHEMICAL_REACTING)

    @JvmField
    val CHEMICAL_WASHING: HTDeferredRecipeType<HTItemOrFluidRecipe.Serializable> = REGISTER.registerType(RagiumConst.CHEMICAL_WASHING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTMixingRecipe.Serializable> = REGISTER.registerType(RagiumConst.MIXING)

    // Machine - Ultimate
    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTEnchantingRecipe.Serializable> = REGISTER.registerType(RagiumConst.ENCHANTING)
}
