package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.api.recipe.HTItemToChancedRecipe
import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
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
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val PRESSING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemAndItemRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PRESSING)

    @JvmField
    val PRINTING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemAndItemRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PRINTING)

    @JvmField
    val WIRING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.WIRING)

    // Machine - Heat
    @JvmField
    val DISTILLING: HTDeferredRecipeType<HTSingleFluidRecipeInput, HTDistillingRecipe> =
        REGISTER.registerType(RagiumConst.DISTILLING)

    @JvmField
    val MELTING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.PYROLYZING)

    // Machine - Cool
    @JvmField
    val FREEZING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.FREEZING)

    // Machine - Chemical
    @JvmField
    val CANNING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        REGISTER.registerType(RagiumConst.CANNING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTChemicalRecipeInput, HTMixingRecipe> =
        REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTWashingRecipe> =
        REGISTER.registerType(RagiumConst.WASHING)

    // Machine - Matter

    // Device
    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTEnchantingRecipe.Input, HTEnchantingRecipe> =
        REGISTER.registerType(RagiumConst.ENCHANTING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTPlantingRecipe.Input, HTPlantingRecipe> =
        REGISTER.registerType(RagiumConst.PLANTING)
}
