package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTViewRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
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
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTShapelessRecipeInput, HTAlloyingRecipe> = REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTCrushingRecipe> = REGISTER.registerType(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTCuttingRecipe> = REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val PRESSING: HTDeferredRecipeType<HTViewRecipeInput, HTPressingRecipe> = REGISTER.registerType(RagiumConst.PRESSING)

    // Machine - Heat
    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTDeferredRecipeType<SingleRecipeInput, HTPyrolyzingRecipe> = REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTSingleFluidRecipeInput, HTRefiningRecipe> = REGISTER.registerType(RagiumConst.REFINING)

    @JvmField
    val SOLIDIFYING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTSolidifyingRecipe> = REGISTER.registerType(RagiumConst.SOLIDIFYING)

    // Machine - Chemical
    @JvmField
    val MIXING: HTDeferredRecipeType<HTChemicalRecipeInput, HTMixingRecipe> = REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTWashingRecipe> = REGISTER.registerType(RagiumConst.WASHING)

    // Machine - Matter

    // Device
    @JvmField
    val COMPRESSING: HTDeferredRecipeType<HTCompressingRecipe.Input, HTCompressingRecipe> = REGISTER.registerType(RagiumConst.COMPRESSING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTViewRecipeInput, HTEnchantingRecipe> = REGISTER.registerType(RagiumConst.ENCHANTING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTViewRecipeInput, HTPlantingRecipe> = REGISTER.registerType(RagiumConst.PLANTING)
}
