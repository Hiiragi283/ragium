package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTShapelessRecipeInput, HTAlloyingRecipe> =
        REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.COMPRESSING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PLANTING)

    @JvmField
    val PRESSING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemAndItemRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PRESSING)

    @JvmField
    val PRINTING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemAndItemRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PRINTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.FREEZING)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.REFINING)

    // Machine - Elite
    @JvmField
    val CANNING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.CANNING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTChemicalRecipeInput, HTMixingRecipe> =
        REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTWashingRecipe> =
        REGISTER.registerType(RagiumConst.WASHING)

    // Machine - Ultimate
    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTEnchantingRecipe.Input, HTEnchantingRecipe> =
        REGISTER.registerType(RagiumConst.ENCHANTING)
}
