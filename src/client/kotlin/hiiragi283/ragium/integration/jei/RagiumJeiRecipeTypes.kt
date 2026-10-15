package hiiragi283.ragium.integration.jei

import hiiragi283.lib.integration.jei.HTJeiRecipeHolder
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.IRecipeType
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RTEnchantingRecipe
import hiiragi283.ragium.api.recipe.RTEngravingRecipe
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import hiiragi283.ragium.api.recipe.RTPlantingRecipe
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import hiiragi283.ragium.api.recipe.RTResourceExtractingRecipe
import hiiragi283.ragium.api.recipe.RTWashingRecipe

data object RagiumJeiRecipeTypes {
    @JvmStatic
    private inline fun <reified T : Any> create(name: String): HTRecipeHolderType<T> =
        IRecipeType<HTJeiRecipeHolder<T>>(RagiumAPI.id(name))

    // Mechanical
    @JvmField
    val ASSEMBLING: HTRecipeHolderType<HTSizedShapelessRecipe.Basic> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val CENTRIFUGING: HTRecipeHolderType<RTCentrifugingRecipe.Basic> = create(RagiumConstants.CENTRIFUGING)

    @JvmField
    val COMPRESSING: HTRecipeHolderType<HTItemToRecipe.BasicItem> = create(RagiumConstants.COMPRESSING)

    @JvmField
    val CRUSHING: HTRecipeHolderType<HTItemToDoubleItemRecipe.Basic> = create(RagiumConstants.CRUSHING)

    @JvmField
    val CUTTING: HTRecipeHolderType<HTItemToDoubleItemRecipe.Basic> = create(RagiumConstants.CUTTING)

    @JvmField
    val FILLING: HTRecipeHolderType<HTItemAndFluidToRecipe.BasicItem> = create(RagiumConstants.FILLING)

    // Heat
    @JvmField
    val ALLOYING: HTRecipeHolderType<HTSizedShapelessRecipe.Basic> = create(RagiumConstants.ALLOYING)

    @JvmField
    val FREEZING: HTRecipeHolderType<RTFreezingRecipe> = create(RagiumConstants.FREEZING)

    @JvmField
    val MELTING: HTRecipeHolderType<HTItemToRecipe.BasicFluid> = create(RagiumConstants.MELTING)

    @JvmField
    val PYROLYZING: HTRecipeHolderType<HTItemToRecipe.BasicItemAndFluid> = create(RagiumConstants.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeHolderType<HTFluidToRecipe.BasicItemAndFluid> = create(RagiumConstants.REFINING)

    // Chemical
    @JvmField
    val BATHING: HTRecipeHolderType<HTItemAndFluidToRecipe.BasicItem> = create(RagiumConstants.BATHING)

    @JvmField
    val MIXING: HTRecipeHolderType<RTMixingRecipe> = create(RagiumConstants.MIXING)

    @JvmField
    val REACTING: HTRecipeHolderType<RTReactingRecipe> = create(RagiumConstants.REACTING)

    @JvmField
    val WASHING: HTRecipeHolderType<RTWashingRecipe> = create(RagiumConstants.WASHING)

    // Resource
    @JvmField
    val BREWING: HTRecipeHolderType<HTItemAndFluidToRecipe.BasicFluid> = create(RagiumConstants.BREWING)

    @JvmField
    val PLANTING: HTRecipeHolderType<RTPlantingRecipe> = create(RagiumConstants.PLANTING)

    // Electronics
    @JvmField
    val ELECTROLYZING: HTRecipeHolderType<RTElectrolyzingRecipe> = create(RagiumConstants.ELECTROLYZING)

    @JvmField
    val ENGRAVING: HTRecipeHolderType<RTEngravingRecipe> = create(RagiumConstants.ENGRAVING)

    @JvmField
    val RESOURCE_EXTRACTING: HTRecipeHolderType<RTResourceExtractingRecipe> =
        create(RagiumConstants.RESOURCE_EXTRACTING)

    // Arcane
    @JvmField
    val ENCHANTING: HTRecipeHolderType<RTEnchantingRecipe> = create(RagiumConstants.ENCHANTING)
}
