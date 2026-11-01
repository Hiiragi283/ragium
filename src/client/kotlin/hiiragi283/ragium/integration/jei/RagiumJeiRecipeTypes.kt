package hiiragi283.ragium.integration.jei

import hiiragi283.lib.integration.jei.IRecipeType
import hiiragi283.lib.recipe.viewer.HTCatalysedSingleToSingleViewerRecipe
import hiiragi283.lib.recipe.viewer.HTDoubleToSingleViewerRecipe
import hiiragi283.lib.recipe.viewer.HTFluidSeparatingViewerRecipe
import hiiragi283.lib.recipe.viewer.HTItemOrFluidViewerRecipe
import hiiragi283.lib.recipe.viewer.HTItemSeparatingViewerRecipe
import hiiragi283.lib.recipe.viewer.HTMultiToSingleViewerRecipe
import hiiragi283.lib.recipe.viewer.HTSingleToMultiViewerRecipe
import hiiragi283.lib.recipe.viewer.HTSingleToSingleViewerRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.recipe.viewer.RTMixingViewerRecipe
import hiiragi283.ragium.recipe.viewer.RTReactingViewerRecipe
import mezz.jei.api.recipe.types.IRecipeType

data object RagiumJeiRecipeTypes {
    @JvmStatic
    private inline fun <reified T : Any> create(name: String): IRecipeType<T> = IRecipeType<T>(RagiumAPI.id(name))

    // Mechanical
    @JvmField
    val ASSEMBLING: IRecipeType<HTMultiToSingleViewerRecipe> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val CENTRIFUGING: IRecipeType<HTItemSeparatingViewerRecipe> = create(RagiumConstants.CENTRIFUGING)

    @JvmField
    val COMPRESSING: IRecipeType<HTSingleToSingleViewerRecipe> = create(RagiumConstants.COMPRESSING)

    @JvmField
    val CRUSHING: IRecipeType<HTSingleToMultiViewerRecipe> = create(RagiumConstants.CRUSHING)

    @JvmField
    val CUTTING: IRecipeType<HTSingleToMultiViewerRecipe> = create(RagiumConstants.CUTTING)

    @JvmField
    val FILLING: IRecipeType<HTDoubleToSingleViewerRecipe> = create(RagiumConstants.FILLING)

    @JvmField
    val PRESSING: IRecipeType<HTCatalysedSingleToSingleViewerRecipe> = create(RagiumConstants.PRESSING)

    // Heat
    @JvmField
    val ALLOYING: IRecipeType<HTMultiToSingleViewerRecipe> = create(RagiumConstants.ALLOYING)

    @JvmField
    val DRYING: IRecipeType<HTItemOrFluidViewerRecipe> = create(RagiumConstants.DRYING)

    @JvmField
    val FREEZING: IRecipeType<HTCatalysedSingleToSingleViewerRecipe> = create(RagiumConstants.FREEZING)

    @JvmField
    val MELTING: IRecipeType<HTSingleToSingleViewerRecipe> = create(RagiumConstants.MELTING)

    @JvmField
    val PYROLYZING: IRecipeType<HTItemSeparatingViewerRecipe> = create(RagiumConstants.PYROLYZING)

    @JvmField
    val REFINING: IRecipeType<HTFluidSeparatingViewerRecipe> = create(RagiumConstants.REFINING)

    // Chemical
    @JvmField
    val BATHING: IRecipeType<HTDoubleToSingleViewerRecipe> = create(RagiumConstants.BATHING)

    @JvmField
    val MIXING: IRecipeType<RTMixingViewerRecipe> = create(RagiumConstants.MIXING)

    @JvmField
    val REACTING: IRecipeType<RTReactingViewerRecipe> = create(RagiumConstants.REACTING)

    // Resource
    @JvmField
    val BREWING: IRecipeType<HTDoubleToSingleViewerRecipe> = create(RagiumConstants.BREWING)

    @JvmField
    val PLANTING: IRecipeType<HTSingleToSingleViewerRecipe> = create(RagiumConstants.PLANTING)

    @JvmField
    val RESOURCE_PUMPING: IRecipeType<HTSingleToSingleViewerRecipe> = create(RagiumConstants.RESOURCE_PUMPING)

    // Electronics
    @JvmField
    val ENGRAVING: IRecipeType<HTCatalysedSingleToSingleViewerRecipe> = create(RagiumConstants.ENGRAVING)

    // Arcane
    @JvmField
    val ENCHANTING: IRecipeType<HTDoubleToSingleViewerRecipe> = create(RagiumConstants.ENCHANTING)

    @JvmField
    val TRANSFORMING: IRecipeType<HTCatalysedSingleToSingleViewerRecipe> = create(RagiumConstants.TRANSFORMING)
}
